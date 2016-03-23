package com.github.tonydeng.websocket.handler;


import com.google.common.base.Joiner;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by tonydeng on 16/3/23.
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestHandler.class);
    private final String wsUri;

    public HttpRequestHandler(String wsUri) {
        super();
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg)
            throws Exception {
        if (wsUri.equalsIgnoreCase(msg.getUri())) {
            ctx.fireChannelRead(msg.retain());
        } else {
            if (HttpHeaders.is100ContinueExpected(msg)) {
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(response);
            }

//            RandomAccessFile file = new RandomAccessFile(HttpRequestHandler.class.getResource("/").getPath() + "/index.html", "r");
            RandomAccessFile file = new RandomAccessFile(
                    Joiner.on(File.separator)
                            .join(new String[]{System.getProperty("user.dir"),"src","main","resources","index.html"}),
                    "r"
            );
            HttpResponse response = new DefaultHttpResponse(msg.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");

            boolean isKeepAlive = HttpHeaders.isKeepAlive(msg);
            if (isKeepAlive) {
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }

            ctx.write(response);
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }

            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (isKeepAlive == false) {
                future.addListener(ChannelFutureListener.CLOSE);
            }

            file.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace(System.err);
    }
}
