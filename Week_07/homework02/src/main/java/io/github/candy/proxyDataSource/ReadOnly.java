package io.github.candy.proxyDataSource;

import java.lang.annotation.*;

/**
 *
 * @author candy
 * @date 2020/12/3
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReadOnly {
    String value() default "slave";
}
