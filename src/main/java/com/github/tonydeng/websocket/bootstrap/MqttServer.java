package com.github.tonydeng.websocket.bootstrap;

import com.github.tonydeng.websocket.init.MqttServerInitalizer;
import io.moquette.BrokerConstants;
import io.moquette.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Created by tonydeng on 16/4/5.
 */
public class MqttServer extends Server {
    private static final Logger log = LoggerFactory.getLogger(MqttServer.class);

    public static void main(String[] args) throws IOException {
        Properties cfg = new Properties();
        cfg.put(BrokerConstants.HOST_PROPERTY_NAME, "localhost");
        cfg.put(BrokerConstants.PORT_PROPERTY_NAME, "9999");
        final MqttServer server = new MqttServer();
        server.startServer(cfg);
        if(log.isInfoEnabled())
            log.info("mqtt server start......");

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                server.stopServer();
            }
        });
    }
}
