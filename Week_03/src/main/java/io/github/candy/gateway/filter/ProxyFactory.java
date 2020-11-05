package io.github.candy.gateway.filter;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by candy on 2020/11/5.
 */
public class ProxyFactory {

    private ProxyFactory(){

    }

    private Logger logger = LoggerFactory.getLogger(ProxyFactory.class);

    private static ProxyFactory instance;


    public static synchronized ProxyFactory build(){
        if(instance == null){
            instance = new ProxyFactory();
        }
        return instance;
    }

    private Map<String,Object> proxyCache = new HashMap<>();


    public <T> T getCacheProxy(T t){
        InvocationHandler handler =  new HeaderProxy(t);
        return (T) Proxy.newProxyInstance(handler.getClass().getClassLoader(),t.getClass().getInterfaces(),handler);
    }

    public <T> T getCacheProxy(Class<T> clazz){
        String key = clazz.getName();
        if(proxyCache.containsKey(key))return (T) proxyCache.get(key);
        InvocationHandler handler = null;
        try {
            handler = new HeaderProxy(clazz.newInstance());
            T t = (T) Proxy.newProxyInstance(handler.getClass().getClassLoader(),clazz.getInterfaces(),handler);
            proxyCache.put(key,t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
