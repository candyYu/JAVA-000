package io.kimmking.rpcfx.rpc;

import io.kimmking.rpcfx.api.RpcfxException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultPromise;

import java.net.URI;
import java.net.URL;

/**
 * Created by candy on 2020/12/18.
 */
public class NettyHttpClientUtils {

    public static String httpPostJson(final String json, final String url) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        DefaultPromise<String> promise = new DefaultPromise<String>(new DefaultEventLoop());
        try {
            URL finalURL = new URL(url);
            Bootstrap bs = new Bootstrap();

            bs.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 处理来自服务端的响应信息
                            socketChannel.pipeline().addLast(new HttpClientCodec());
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                            socketChannel.pipeline().addLast(new HttpContentDecompressor());
                            socketChannel.pipeline().addLast(new ClientHandler(promise, json, url));
                        }
                    });
            // 客户端开启
            ChannelFuture cf = bs.connect(finalURL.getHost(), finalURL.getPort()).sync();
            cf.channel().closeFuture().sync();
            return  promise.get();
        }catch (Exception ex) {
            throw  new RpcfxException(ex);
        }finally {
            bossGroup.shutdownGracefully().sync();
        }


    }

    private static class ClientHandler extends ChannelInboundHandlerAdapter {

        private  DefaultPromise defaultPromise;

        private String requestJson;

        private String url;

        public ClientHandler(DefaultPromise promise, String requestJson, String url) {
            this.defaultPromise = promise;
            this.requestJson = requestJson;
            this.url = url;
        }


        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            URI uri = new URI( url);
            System.out.println("requestJson: "+requestJson);
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
            request.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json");
            request.headers().add(HttpHeaderNames.ACCEPT, "application/json");
            request.headers().add(HttpHeaderNames.HOST, uri.getHost()+":"+uri.getPort());
            request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            request.content().writeBytes( Unpooled.wrappedBuffer(requestJson.getBytes("UTF-8")));
            request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
            ctx.writeAndFlush(request);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            if(msg instanceof FullHttpResponse){
                FullHttpResponse response = (FullHttpResponse)msg;
                ByteBuf buf = response.content();
                String result = buf.toString(CharsetUtil.UTF_8);
                System.out.println("response -> "+result);
                defaultPromise.setSuccess(result);

            }


        }


        // 数据读取完毕的处理
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.err.println("客户端读取数据完毕");
            ctx.close();
        }

        // 出现异常的处理
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.err.println("client 读取数据出现异常");
            ctx.close();
        }


    }
}
