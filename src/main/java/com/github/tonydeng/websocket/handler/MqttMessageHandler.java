package com.github.tonydeng.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 16/4/5.
 */
public class MqttMessageHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private static final Logger log = LoggerFactory.getLogger(MqttMessageHandler.class);


    @Override
    public boolean isSharable() {
        return true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (log.isInfoEnabled())
            log.info("mqtt channel active.....");
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_LEAST_ONCE, false, 0);
        MqttConnectMessage connectMessage = new MqttConnectMessage(fixedHeader, null, null);
        ctx.channel().writeAndFlush(connectMessage);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
        if(log.isInfoEnabled())
            log.info("mqtt channel messageReceived......");
        switch (msg.fixedHeader().messageType()){
            case CONNECT:
                log.info("mqtt CONNECT message:'{}'",msg);
                break;
            case SUBSCRIBE:
                log.info("mqtt CONNECT message:'{}'",msg);
                break;
            case PUBLISH:
                log.info("mqtt PUBLISH message:'{}'",msg);
                break;
            case PUBACK:
                log.info("mqtt PUBACK message:'{}'",msg);
                break;
            case PINGREQ:
                log.info("mqtt PINGREQ message:'{}'",msg);
                break;
            default:
                log.info("mqtt default message:'{}'",msg);
        }
    }
}
