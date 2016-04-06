package com.github.tonydeng.websocket.init;

import com.github.tonydeng.websocket.handler.HttpRequestHandler;
import com.github.tonydeng.websocket.handler.TextWebSocketFrameHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 16/3/23.
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {
    private static final Logger log = LoggerFactory.getLogger(ChatServerInitializer.class);
    private final ChannelGroup group;
    public ChatServerInitializer(ChannelGroup group) {
        super();
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        if(log.isInfoEnabled())
            log.info("chat server init channel......");
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());

        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new HttpObjectAggregator(64*1024));

        pipeline.addLast(new HttpRequestHandler("/ws"));

        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new TextWebSocketFrameHandler(group));
    }
}
