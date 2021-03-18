package org.jiang.core.annotations;

import org.jiang.core.enums.LogType;

import java.lang.annotation.*;

/**
 * 操作日志
 * @author 蒋小胖
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    /**
     * 日志类型
     */
    LogType logType() default LogType.OPERATION_LOG;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default false;

    /**
     * 是否保存响应的结果
     */
    boolean isSaveResponseData() default false;
}
