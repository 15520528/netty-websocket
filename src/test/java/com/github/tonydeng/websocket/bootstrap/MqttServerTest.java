package com.github.tonydeng.websocket.bootstrap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 16/4/6.
 */
public class MqttServerTest {
    private static final Logger log = LoggerFactory.getLogger(MqttServerTest.class);

    @Test
    public void testMain(){
        final MqttServer server = new MqttServer();

        server.main(new String[0]);
    }
}
