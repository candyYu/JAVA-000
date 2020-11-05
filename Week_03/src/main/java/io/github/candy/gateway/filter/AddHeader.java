package io.github.candy.gateway.filter;

import java.lang.annotation.*;

/**
 * Created by candy on 2020/11/5.
 */

@Repeatable(AddHeaders.class)
public @interface AddHeader{

    String name() default  "nio";


    String value() default "candy";

}
