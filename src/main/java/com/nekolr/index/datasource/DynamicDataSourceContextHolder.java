package com.nekolr.index.datasource;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源上下文
 *
 * @author nekolr
 */
public class DynamicDataSourceContextHolder {

    /**
     * 数据源上下文，持有当前线程的数据源名称
     */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 数据源名称集合
     */
    private static List<String> dataSourceKeys = new ArrayList<>();

    /**
     * 设置当前线程的数据源名称
     *
     * @param dataSourceKey
     */
    public static void setDataSourceKey(String dataSourceKey) {
        contextHolder.set(dataSourceKey);
    }

    /**
     * 获取当前线程的数据源名称
     *
     * @return
     */
    public static String getContextKey() {
        return contextHolder.get();
    }

    /**
     * 删除当前线程的数据源名称
     */
    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    /**
     * 判断是否存在这个数据源
     *
     * @param dataSourceKey 数据源名称
     * @return
     */
    public static boolean containsDataSource(String dataSourceKey) {
        return dataSourceKeys.contains(dataSourceKey);
    }

    /**
     * 获取 dataSourceKeys
     *
     * @return
     */
    public static List<String> getDataSourceKeys() {
        return dataSourceKeys;
    }
}
