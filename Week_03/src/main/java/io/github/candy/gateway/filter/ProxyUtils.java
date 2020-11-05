package io.github.candy.gateway.filter;


/**
 * Created by candy on 2020/11/5.
 */
public class ProxyUtils {

    public static  <T> T getProxy(Class<T> clazz){
        return ProxyFactory.build().getCacheProxy(clazz);
    }

}
