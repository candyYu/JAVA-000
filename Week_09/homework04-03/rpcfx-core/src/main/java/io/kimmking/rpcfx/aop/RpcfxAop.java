package io.kimmking.rpcfx.aop;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxException;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.rpc.NettyHttpClientUtils;
import io.kimmking.rpcfx.rpc.PoolingHttpClientUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by candy on 2020/12/17.
 */
@Component
@Aspect
public class RpcfxAop  {

    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    private String url;

    public RpcfxAop(String url) {
        this.url = url;
    }

    @Pointcut("execution(* *.*(..))") // 切入点表达式 所有方法都要切 很明显
    public void doAop() {

    }

    @Around(value = "doAop()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws RpcfxException {
        RpcfxRequest request = new RpcfxRequest();
        request.setServiceClass(proceedingJoinPoint.getSignature().getDeclaringTypeName());
        request.setMethod(proceedingJoinPoint.getSignature().getName());
        request.setParams(proceedingJoinPoint.getArgs());


        RpcfxResponse response = post(request, url);;
        if( !response.isStatus()) {
            throw  new RpcfxException("请求出错");
        }
        return response.getResult();
    }


    private RpcfxResponse post(RpcfxRequest req, String url) throws RpcfxException {
        // 1.可以复用client
        // 2.尝试使用httpclient或者netty client
//        return  usePoolingHttpClient(req, url);
        return  useNettyClient(req, url);
//        return  useOKHttpClient(req, url);
    }

    private RpcfxResponse useNettyClient(RpcfxRequest req, String url) throws RpcfxException {
        try {
        String reqJson = JSON.toJSONString(req);
        String respJson = NettyHttpClientUtils.httpPostJson(reqJson, url);
        return JSON.parseObject(respJson, RpcfxResponse.class) ;
        }catch (Exception ex){
            throw  new RpcfxException(ex);
        }
    }

    private RpcfxResponse usePoolingHttpClient(RpcfxRequest req, String url) throws RpcfxException {
        try {
            String reqJson = JSON.toJSONString(req);
            String respJson = PoolingHttpClientUtils.httpPostJson(reqJson, url);
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }catch (Exception ex){
            throw  new RpcfxException(ex);
        }
    }

    private RpcfxResponse useOKHttpClient(RpcfxRequest req, String url) throws RpcfxException{
        String respJson = null;
        try {
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: " + reqJson);
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSONTYPE, reqJson))
                    .build();
            respJson = client.newCall(request).execute().body().string();
            System.out.println("resp json: " + respJson);
        }catch (IOException e) {
            throw  new RpcfxException(e);
        }
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }
}
