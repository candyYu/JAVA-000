package io.github.candy.gateway.handler;


import io.github.candy.gateway.filter.FilterHandler;
import io.github.candy.gateway.filter.HeaderProxy;
import io.github.candy.gateway.filter.ProxyUtils;
import io.github.candy.gateway.proxy.httpclient.HttpOutboundHandler;
import io.github.candy.gateway.proxy.httpclient4.HttpClient4OutBoundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by candy on 2020/11/2.
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter{

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private FilterHandler handler;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        handler = ProxyUtils.getProxy(HttpOutboundHandler.class);
        handler.setBackendUrl(proxyServer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            handler.handle(fullRequest, ctx);

        }catch (Exception ex) {
            ex.printStackTrace();
            ReferenceCountUtil.release(msg);
        }


    }
}
