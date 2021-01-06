package io.kimmking.rpcfx.proxy;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.rpc.PoolingHttpClientUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


/**
 * Created by candy on 2020/12/21.
 */
public class GCLibProxy {

    public static <T> T create(final Class<T> serviceClass, final String url) {

        return  (T) new ProxySubject(serviceClass,url).getProxy();
    }

    /**
     * 10  * 代理类
     * 11  * Created by Kevin on 2017/11/6.
     * 12
     */
    public static class ProxySubject implements MethodInterceptor {
        private Enhancer enhancer = new Enhancer();

        private final Class<?> serviceClass;
        private final String url;

        public <T> ProxySubject(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        public Object getProxy() {
            enhancer.setSuperclass(serviceClass);
            enhancer.setCallback(this);
            return enhancer.create();
            //用于创建无参的目标对象代理类，对于有参构造器则调用Enhancer.create(Class[] argumentTypes, Object[] arguments)，第一个参数表示参数类型，第二个参数表示参数的值。
        }

        @Override
        public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(args);
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
