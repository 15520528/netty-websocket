package com.github.tonydeng.websocket.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 16/4/7.
 */
public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static final String HOST = "tcp://localhost:9000";

    public static final String TOPIC = "t/test";

    public static final String clientid = "server";

    private MqttClient client;

    private MqttTopic topic;
    private String user = "tonydeng";
    private String password = "123456";

    private MqttMessage message;

    public Server() throws MqttException {
        this.client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();

        options.setCleanSession(false);
        options.setUserName(user);
        options.setPassword(password.toCharArray());

        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);

        client.setCallback(new PushCallback());
        client.connect(options);
        topic = client.getTopic(TOPIC);
    }

    public void publish(MqttMessage message) throws MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();

        log.info("is complete:'{}'", token.isComplete());
    }

    public static void main(String[] args) throws MqttException {
        Server server = new Server();

        server.message = new MqttMessage();
        server.message.setQos(1);
        server.message.setPayload("mqtt message".getBytes());

        server.publish(server.message);

        log.info("server ratained状态:'{}'", server.message.isRetained());
    }
}
