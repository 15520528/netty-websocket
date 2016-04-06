package com.github.tonydeng.websocket.bootstrap;

import com.github.tonydeng.websocket.init.MqttServerInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 16/4/5.
 */
public class MqttServer {
    private static final Logger log = LoggerFactory.getLogger(MqttServer.class);

    private final EventLoopGroup boss = new NioEventLoopGroup();

    private final EventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    private static final int port = 2015;

    public void start(){

        ServerBootstrap boot = new ServerBootstrap();

        boot.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new MqttServerInitalizer());
    }

    public static void main(String[] args){

    }
}
