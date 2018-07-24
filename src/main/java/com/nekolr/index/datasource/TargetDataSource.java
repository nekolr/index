package com.nekolr.index.datasource;

import java.lang.annotation.*;

/**
 * 目标数据源注解
 *
 * @author nekolr
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface TargetDataSource {
    String key();
}
