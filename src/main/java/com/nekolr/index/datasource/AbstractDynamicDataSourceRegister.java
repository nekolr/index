package com.nekolr.index.datasource;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册动态数据源时，默认数据源需要手动注册
 */
public class AbstractDynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    /**
     * 默认数据源配置名称
     */
    public static final String DEFAULT_PROPERTY_DATASOURCE_TYPE = "spring.datasource.type";

    /**
     * 默认数据源配置前缀
     */
    public static final String DEFAULT_PROPERTY_DATASOURCE_PREFIX = "spring.datasource";

    /**
     * 自定义数据源配置前缀
     */
    public static final String CUSTOM_PROPERTY_DATASOURCE_PREFIX = "custom.datasource";

    /**
     * 自定义数据源配置前缀
     */
    public static final String CUSTOM_PROPERTY_DATASOURCE_PREFIX_PLUS = "custom.datasource.";

    /**
     * 别名处理
     */
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;

    /**
     * 自定义数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<>();

    /**
     * 使用动态数据源需要手动初始化默认数据源
     *
     * @param environment
     */
    protected void initDefaultDataSource(Environment environment) {
        String dataSourceType = environment.getProperty(DEFAULT_PROPERTY_DATASOURCE_TYPE);
        if (dataSourceType == null || dataSourceType.length() == 0) {
            throw new RuntimeException("DataSource type must not be null");
        }
        Map<String, Object> defaultConfig = Binder.get(environment).bind(DEFAULT_PROPERTY_DATASOURCE_PREFIX, Map.class).get();
        Class<? extends DataSource> type = getDataSourceType(dataSourceType);
        defaultDataSource = bind(type, defaultConfig);
    }

    /**
     * 初始化其他数据源
     *
     * @param environment
     */
    protected void initCustomDataSource(Environment environment) {
        Map<String, Object> customConfigs = Binder.get(environment).bind(CUSTOM_PROPERTY_DATASOURCE_PREFIX, Bindable.mapOf(String.class, Object.class)).get();
        customConfigs.forEach((k, v) -> {
            // 是否有继承配置
            Boolean extend = Boolean.valueOf(environment.getProperty(CUSTOM_PROPERTY_DATASOURCE_PREFIX_PLUS + k + ".extend"));
            if (extend) {
                Map<String, Object> defaultConfig = Binder.get(environment).bind(DEFAULT_PROPERTY_DATASOURCE_PREFIX, Map.class).get();
                Map<String, Object> customConfig = Binder.get(environment).bind(CUSTOM_PROPERTY_DATASOURCE_PREFIX_PLUS + k, Map.class).get();
                defaultConfig.putAll(customConfig);
                String dataSourceType = environment.getProperty(DEFAULT_PROPERTY_DATASOURCE_TYPE);
                Class<? extends DataSource> type = getDataSourceType(dataSourceType);
                // 覆盖配置
                customDataSources.put(k, bind(type, defaultConfig));
            } else {
                String dataSourceType = environment.getProperty(CUSTOM_PROPERTY_DATASOURCE_PREFIX_PLUS + k + ".type");
                if (dataSourceType == null || dataSourceType.length() == 0) {
                    throw new RuntimeException("DataSource type must not be null");
                }
                Map<String, Object> customConfig = Binder.get(environment).bind(CUSTOM_PROPERTY_DATASOURCE_PREFIX_PLUS + k, Map.class).get();
                Class<? extends DataSource> type = getDataSourceType(dataSourceType);
                customDataSources.put(k, bind(type, customConfig));
            }
        });
    }

    /**
     * 通过全限定名反射创建类对象
     *
     * @param dataSourceType 数据源类型全限定名
     * @return
     */
    private Class<? extends DataSource> getDataSourceType(String dataSourceType) {
        Class<? extends DataSource> type;
        try {
            type = (Class<? extends DataSource>) Class.forName(dataSourceType);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("DataSource type is not found");
        }
        return type;
    }

    /**
     * 绑定配置到数据源
     *
     * @param dataSource
     * @param config
     */
    private void bind(DataSource dataSource, Map<String, Object> config) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(config);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));
    }

    /**
     * 绑定配置到指定的类型
     *
     * @param type   指定的类型
     * @param config 配置
     * @param <T>
     * @return
     */
    private <T extends DataSource> T bind(Class<T> type, Map<String, Object> config) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(config);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(type)).get();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.initDefaultDataSource(environment);
        this.initCustomDataSource(environment);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {

        Map<String, Object> targetDataSources = new HashMap<>((int) (customDataSources.size() / 0.75) + 1);
        // 放入默认数据源
        targetDataSources.put("dataSource", defaultDataSource);
        // 放入其他数据源
        targetDataSources.putAll(customDataSources);
        // 添加到动态数据源上下文中
        targetDataSources.forEach((k, v) -> DynamicDataSourceContextHolder.getDataSourceKeys().add(k));
        // 通用 bean 定义
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        // 获取所有属性配置
        MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();
        // 设置全部目标数据源属性
        mutablePropertyValues.addPropertyValue("targetDataSources", targetDataSources);
        // 设置默认数据源属性
        mutablePropertyValues.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        // 此处必须是 AbstractRoutingDataSource 的子类
        beanDefinition.setBeanClass(DynamicDataSource.class);

        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);
    }

    static {
        /**
         * 部分数据源配置不同，所以在此处添加别名，避免切换数据源出现某些参数无法注入的情况
         */
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }
}
