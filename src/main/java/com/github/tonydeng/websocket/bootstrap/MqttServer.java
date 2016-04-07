package com.github.tonydeng.websocket.bootstrap;

import com.github.tonydeng.websocket.init.MqttServerInitalizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by tonydeng on 16/4/5.
 */
public class MqttServer {
    private static final Logger log = LoggerFactory.getLogger(MqttServer.class);

    private final EventLoopGroup boss = new NioEventLoopGroup();

    private final EventLoopGroup worker = new NioEventLoopGroup();

    private Channel channel;

    private static final int port = 2015;

    public ChannelFuture start(InetSocketAddress address) {

        ServerBootstrap boot = new ServerBootstrap();

        boot.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new MqttServerInitalizer());

        try {
            ChannelFuture f = boot.bind(address).sync();
            if (log.isInfoEnabled())
                log.info("server start port:'{}'", port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
        return null;
    }
    public void destroy() {
        if (log.isInfoEnabled())
            log.info("chat server destroy......");
        if (channel != null)
            channel.close();
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }



    public static void main(String[] args) {
        final MqttServer server = new MqttServer();
        ChannelFuture f = server.start(new InetSocketAddress(port));
        if (log.isInfoEnabled())
            log.info("chat server start................");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if(log.isInfoEnabled())
                    log.info("chat server shutdown.......");
                server.destroy();
            }
        });
        f.channel().closeFuture().syncUninterruptibly();
    }
}
