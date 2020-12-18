package io.kimmking.rpcfx.server;

import io.kimmking.rpcfx.api.*;
import io.kimmking.rpcfx.common.InvokerFactory;

public class RpcfxInvoker {

    private RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver){
        this.resolver = resolver;
    }


    public RpcfxResponse invoke(RpcfxRequest request) {
        RpcfxResponse response = new RpcfxResponse();
        String serviceClass = request.getServiceClass();
        try {
            Object service = resolver.resolve(serviceClass);
            RpcInvoker rpcInvoker = InvokerFactory.getInvoker(service.getClass());
            Object result = rpcInvoker.invoke(service, request.getMethod(), request.getParams());
            response.setResult(result);
            response.setStatus(true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setException(new RpcfxException(e));
            response.setStatus(false);
            return response;
        }
    }

//    public RpcfxResponse invoke(RpcfxRequest request) {
//        RpcfxResponse response = new RpcfxResponse();
//        String serviceClass = request.getServiceClass();
//
//        // 作业1：改成泛型和反射
//        Object service = resolver.resolve(serviceClass);
//
//        try {
//            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
//            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
//            // 两次json序列化能否合并成一个
//            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
//            response.setStatus(true);
//            return response;
//        } catch ( IllegalAccessException | InvocationTargetException e) {
//
//            RpcfxException rpcfxException = new RpcfxException(e);
//            // 3.Xstream
//            // 2.封装一个统一的RpcfxException
//            // 客户端也需要判断异常
//            e.printStackTrace();
//            response.setException(rpcfxException);
//            response.setStatus(false);
//            return response;
//        }
//    }
//
//    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
//        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
//    }

}
