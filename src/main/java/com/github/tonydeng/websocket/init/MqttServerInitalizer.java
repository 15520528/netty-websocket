package com.github.tonydeng.websocket.init;

import com.github.tonydeng.websocket.handler.MqttMessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by tonydeng on 16/4/6.
 */
public class MqttServerInitalizer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("idle",new IdleStateHandler(180,200,200));
        pipeline.addLast("encode",new MqttEncoder());
        pipeline.addLast("decode",new MqttDecoder());
        pipeline.addLast(new MqttMessageHandler());
    }
}
