package io.github.candy.gateway.filter;

import io.netty.handler.codec.http.FullHttpRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by candy on 2020/11/5.
 */
public class HeaderProxy implements InvocationHandler {


    private Object subject;


    public HeaderProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = null;
        try {
            AddHeaders addHeaders = getHeader(method);
            if (addHeaders != null && addHeaders.value().length > 0) {
                //获取实际类调用参数的第一个参数
                if (args.length > 0) {
                    if (args[0] instanceof FullHttpRequest) {
                        FullHttpRequest req = (FullHttpRequest)args[0];
                        for (AddHeader addHeader : addHeaders.value()) {
                            req.headers().add(addHeader.name(), addHeader.value());
                        }

                    }
                }
            }


            result = method.invoke(subject, args);
        }catch (Exception e){
            e.printStackTrace();
            throw e.getCause();
        }

        return result;
    }

    private AddHeaders getHeader(Method method) throws NoSuchMethodException {
        return subject.getClass().getMethod(method.getName(),method.getParameterTypes()).getAnnotation(AddHeaders.class);
    }

}
