package io.github.candy.proxyDataSource;

import java.lang.annotation.*;

/**
 * Created by candy on 2020/12/3.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReadOnly {
    String value() default "slave";
}
