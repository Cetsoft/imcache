package com.cetsoft.imcache.cache.redis.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public class Connection implements Closeable {

    private String host;
    private int port = Protocol.DEFAULT_PORT;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private Socket socket;
    private RedisOutputStream outputStream;
    private RedisInputStream inputStream;

    public Connection(String host) {

    }

    public Connection(String host, int port) {

    }

    public Connection(String host, int port, int timeout) {

    }

    @Override
    public void close() throws IOException {

    }
}
