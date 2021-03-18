package org.jiang.core.annotations;

import java.lang.annotation.*;

/**
 * Validate
 * @author 蒋小胖
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateAnnotation {
    /**
     * decide validate
     */
    boolean isValidate() default true;
}
