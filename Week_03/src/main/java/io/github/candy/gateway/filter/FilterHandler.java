package io.github.candy.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created by candy on 2020/11/5.
 */
public interface FilterHandler {

    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx);

    public void setBackendUrl(String backendUrl);
}
