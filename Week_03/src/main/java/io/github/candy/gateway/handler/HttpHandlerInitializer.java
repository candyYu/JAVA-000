package io.github.candy.gateway.handler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Created by candy on 2020/11/2.
 */
public class HttpHandlerInitializer extends ChannelInitializer<SocketChannel> {


    private String proxyServer;

    public HttpHandlerInitializer(String proxyServer) {
        this.proxyServer  = proxyServer;
    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        p.addLast(new HttpInboundHandler(this.proxyServer));
    }
}