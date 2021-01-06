package io.kimmking.rpcfx.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.rpc.PoolingHttpClientUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by candy on 2020/12/21.
 */
public class JDKProxy {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }


    public static <T> T create(final Class<T> serviceClass, final String url) {

        // 0. 替换动态代理 -> AOP
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));

    }


    public static class RpcfxInvocationHandler implements InvocationHandler {

        private final Class<?> serviceClass;
        private final String url;
        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        @Override
        public Object invoke(Object proxy, Method method, Object[] params){
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);
            RpcfxResponse response = post(request, url);
            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(RpcfxRequest req, String url) {
            String respJson = null;
            try {
                String reqJson = JSON.toJSONString(req);
                System.out.println("req json: " + reqJson);

                 respJson = PoolingHttpClientUtils.httpPostJson(reqJson, url);
                System.out.println("resp json: " + respJson);
            }catch (Exception ex){
                RpcfxResponse rpcfxResponse = new RpcfxResponse();
                rpcfxResponse.setStatus(false);
                rpcfxResponse.setException(ex);
                return  rpcfxResponse;
            }
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }
    }

}
