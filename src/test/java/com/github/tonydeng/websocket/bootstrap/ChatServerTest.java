package com.github.tonydeng.websocket.bootstrap;

import com.google.common.base.Joiner;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Created by tonydeng on 16/3/23.
 */
public class ChatServerTest {

    private static final Logger log = LoggerFactory.getLogger(ChatServerTest.class);

//    @Test
    public void testGetPath() throws FileNotFoundException {

        RandomAccessFile file = new RandomAccessFile(
                Joiner.on(File.separator)
                        .join(new String[]{System.getProperty("user.dir"),"src","main","resources","index.html"}),
                "r"
        );
        if(log.isInfoEnabled())
            log.info("user dir path:'{}'   html:'{}'",System.getProperty("user.dir"),file);

    }

    @Test
    public void testMain() {
        ChatServer server = new ChatServer();
        server.main(new String[0]);
    }
}
