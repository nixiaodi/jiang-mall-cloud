package org.jiang.core.annotations;

import java.lang.annotation.*;

/**
 * @see org.jiang.core.interceptor.SqlLogInterceptor
 * 配合 SqlLogInterceptor 对指定方法 禁止打印SQL到控制台
 * @author 蒋小胖
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NotDisplaySql {
}
