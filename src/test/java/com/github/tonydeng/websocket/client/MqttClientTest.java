package com.github.tonydeng.websocket.client;

import com.google.common.base.Stopwatch;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.junit.Test;
import org.msgpack.MessagePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by tonydeng on 16/4/6.
 */
public class MqttClientTest {
    private static final Logger log = LoggerFactory.getLogger(MqttClientTest.class);
    private IMqttClient iclient;
    private MqttClientPersistence dataStore;
    private String topic = "/painter";
    private final int qos = 1;
    private final MessagePack messagePack = new MessagePack();

    private Stopwatch pushStopwatch;
    private Stopwatch callStopwatch;


    //TODO 使用groboutils来进行多线程的并发单元测试
    @Test
    public void testMqttMessage() throws IOException, MqttException {
        connect("tcp://localhost:9999");
        for (int i = 0; i < 100; i++) {
            pushStopwatch = Stopwatch.createStarted();
            MqttMessage mqttMessage = new MqttMessage(messagePack.write("test mqtt"));
            mqttMessage.setQos(qos);
            iclient.publish(topic, mqttMessage);
            pushStopwatch.stop();
            log.info("push time {}ms", pushStopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private void connect(String url) throws MqttException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        dataStore = new MqttDefaultFilePersistence(tmpDir + File.separator + "publisher");
        iclient = new MqttClient(url, System.currentTimeMillis() + "", dataStore);
        iclient.setCallback(new TestCallback());
        iclient.connect();
        iclient.subscribe("/painter", qos);

    }

    class TestCallback implements MqttCallback {

        private boolean m_connectionLost = false;

        @Override
        public void connectionLost(Throwable cause) {
            m_connectionLost = true;
            log.info("connectionLost......");
        }

        @Override
        public void messageArrived(String s, org.eclipse.paho.client.mqttv3.MqttMessage message) throws Exception {
            callStopwatch = Stopwatch.createStarted();

            String msg = messagePack.read(message.getPayload(), String.class);
            callStopwatch.stop();
            log.info("message:'{}' len={}, cost: {}ms", msg, msg.length(), callStopwatch.elapsed(TimeUnit.MILLISECONDS));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            log.info("deliveryComplete......  token:'{}'", token);
        }
    }

}


