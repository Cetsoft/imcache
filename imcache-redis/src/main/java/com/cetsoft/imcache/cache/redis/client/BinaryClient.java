package com.cetsoft.imcache.cache.redis.client;

public class BinaryClient extends Connection {

    public BinaryClient(String host) {
        super(host);
    }

    public BinaryClient(String host, int port) {
        super(host, port);
    }

    public void set(final byte[] key, final byte[] value) {
        sendCommand(Protocol.Command.SET, key, value);
    }
}
