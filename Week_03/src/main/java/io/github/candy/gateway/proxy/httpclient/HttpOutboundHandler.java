package io.github.candy.gateway.proxy.httpclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by candy on 2020/11/2.
 */
public class HttpOutboundHandler {


    private CloseableHttpClient httpclient;
    private ExecutorService proxyService;//使用线程池处理过来的请求，增加效率
    private String backendUrl;
    private Logger logger = LoggerFactory.getLogger(HttpOutboundHandler.class);

    public HttpOutboundHandler(String proxyServer) {

        this.backendUrl = proxyServer.endsWith("/")?proxyServer.substring(0,proxyServer.length()-1):proxyServer;
        int cores = Runtime.getRuntime().availableProcessors() * 2;
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores,
                keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);


        httpclient = HttpClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();




    }

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
        //透传fullRequest给proxyServer
        //可以附加上head等添加各种想要的aop
        final String url = this.backendUrl + fullRequest.uri();
        proxyService.submit(() -> fetchGet(fullRequest, ctx, url));

    }


    private void fetchGetWithCustomResponse(final  FullHttpRequest req, final  ChannelHandlerContext ctx, final String url) {
        String responseText = "hello,candy";
        FullHttpMessage response =  new DefaultFullHttpResponse(HTTP_1_1,OK,Unpooled.wrappedBuffer(responseText.getBytes()));
        response.headers().set("Content-Type", "application/json");
        response.headers().setInt("Content-Length", response.content().readableBytes());
        ctx.write(response);
        ctx.flush();
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        // 创建http GET请求
        logger.info("request url:"+url);
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);

        FullHttpResponse nettyResponse = null;

        CloseableHttpResponse response = null;
        try {

            HttpHeaders nettyHeaders = inbound.headers();

            if(!nettyHeaders.isEmpty()) {
                Iterator<Map.Entry<String,String>> iterator =  nettyHeaders.iteratorAsString();
                while (iterator.hasNext()) {
                    Map.Entry<String,String> entry = iterator.next();
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            byte[] body = EntityUtils.toByteArray(response.getEntity());

            nettyResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.valueOf(response.getStatusLine().getStatusCode()),Unpooled.wrappedBuffer(body));


            HeaderIterator iteratorRes = response.headerIterator();

            while (iteratorRes.hasNext()) {
                Header header = iteratorRes.nextHeader();
                nettyResponse.headers().set(header.getName(), header.getValue());
            }


            //注意一定要设置返回的Content-Type 和Content-Length 不然浏览器侧会hang住
//            nettyResponse.headers().set("Content-Type", "application/json");
//            nettyResponse.headers().setInt("Content-Length", Integer.parseInt(response.getFirstHeader("Content-Length").getValue()));

            //把返回内容包装为response 使用ctx.flush返回出去


        } catch (Exception ex) {
            ex.printStackTrace();
            nettyResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, ex);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inbound != null) {
                if (!HttpUtil.isKeepAlive(inbound)) {
                    ctx.write(nettyResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(nettyResponse);
                }
            }
            ctx.flush();
            System.out.println("flush response");
        }
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
