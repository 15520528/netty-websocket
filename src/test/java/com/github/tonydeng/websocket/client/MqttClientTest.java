package com.github.tonydeng.websocket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.timeout.IdleStateHandler;
import org.eclipse.paho.client.mqttv3.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by tonydeng on 16/4/6.
 */
public class MqttClientTest implements MqttCallback{
    private static final Logger log = LoggerFactory.getLogger(MqttClientTest.class);
    private MqttClient client;
    @Test
    public void testMqttMessage() throws IOException {
        String msg = "test";
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.CONNECT, true, MqttQoS.AT_LEAST_ONCE, true, msg.length());
        MqttMessage mqttMessage = new MqttMessage(header, msg);



    }

    public void run(){
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("idle",new IdleStateHandler(180,200,200));
                        pipeline.addLast("encode",new MqttEncoder());
                        pipeline.addLast("decode",new MqttDecoder());
                    }
                });

    }

    public void doDemo(String tcpUrl,String clientId,String topicName){
        try {
            client = new MqttClient(tcpUrl,clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setConnectionTimeout(300);
            options.setKeepAliveInterval(1000);
            client.connect(options);
            client.setCallback(this);
            client.subscribe(topicName);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connectionLost(Throwable cause){
        cause.printStackTrace();
    }

    public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage mqttMessage){
        log.info("[GOT PUBLISH MESSAGE]:{}",mqttMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}
