package com.github.tonydeng.websocket.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Created by tonydeng on 16/4/7.
 * 发布消息的回调类
 *
 * 必须实现MqttCallback的接口并实现对应的相关接口方法
 *      ◦CallBack 类将实现 MqttCallBack。每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。在回调中，将它用来标识已经启动了该回调的哪个实例。
 *  ◦必须在回调类中实现三个方法：
 *
 *  public void messageArrived(MqttTopic topic, MqttMessage message)
 *  接收已经预订的发布。
 *
 *  public void connectionLost(Throwable cause)
 *  在断开连接时调用。
 *
 *  public void deliveryComplete(MqttDeliveryToken token))
 *      接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 *  ◦由 MqttClient.connect 激活此回调。
 *
 */
public class PushCallback implements MqttCallback {
    private static final Logger log = LoggerFactory.getLogger(PushCallback.class);

    @Override
    public void connectionLost(Throwable throwable) {
        log.info("连接断开.......");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log.info("topic:{}", s);
        log.info("Qos:{}", mqttMessage.getQos());
        log.info("content:'{}'", mqttMessage.getPayload());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("devliver complete", iMqttDeliveryToken.isComplete());
    }
}
