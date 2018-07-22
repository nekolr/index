package com.nekolr.index.datasource;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 动态数据源切面
 */
@Aspect
@Order(-10) // 保证该 AOP 在 @Transactional 之前执行
@Component
@Slf4j
public class DynamicDataSourceAspect {

    /**
     * 修改默认的数据源为指定的数据源，在目标数据源使用前执行
     *
     * @param point
     * @param targetDataSource
     */
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        String dataSourceKey = targetDataSource.key();
        if (!DynamicDataSourceContextHolder.containsDataSource(dataSourceKey)) {
            log.info("{} 使用数据源 [{}] 不存在，采用默认数据源", point.getSignature(), targetDataSource);
        } else {
            log.info("{} 使用数据源 [{}]", point.getSignature(), targetDataSource);
            DynamicDataSourceContextHolder.setDataSourceKey(dataSourceKey);
        }
    }

    /**
     * 恢复默认的数据源，在目标数据源使用后执行
     *
     * @param point
     * @param targetDataSource
     */
    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        if (!DynamicDataSourceContextHolder.containsDataSource(targetDataSource.key())) {
            log.info("{} 使用数据源完成，切换回默认数据源", point.getSignature());
        } else {
            log.info("{} 使用数据源完成，数据源由 [{}] 切换回默认数据源", point.getSignature(), targetDataSource);
        }
        DynamicDataSourceContextHolder.clearDataSourceKey();
    }
}
