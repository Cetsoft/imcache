package com.cetsoft.imcache.cache.redis.client;

import com.cetsoft.imcache.cache.redis.client.exception.RedisConnectionException;
import com.cetsoft.imcache.cache.redis.client.util.RedisInputStream;
import com.cetsoft.imcache.cache.redis.client.util.RedisOutputStream;
import com.cetsoft.imcache.cache.redis.client.util.SafeEncoder;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Closeable {

    private String host;
    private int port = Protocol.DEFAULT_PORT;
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private Socket socket;
    private RedisOutputStream outputStream;
    private RedisInputStream inputStream;

    private boolean broken = false;

    public Connection() {

    }

    public Connection(String host) {
        super();
        this.host = host;
    }

    public Connection(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setTimeoutInfinite() {
        try {
            if (!isConnected())
                connect();
            socket.setKeepAlive(true);
            socket.setSoTimeout(0);
        } catch (SocketException e) {
            broken = true;
            throw new RedisConnectionException(e);
        }
    }

    public void rollbackTimeout(int timeout) {
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(timeout);
        } catch (SocketException e) {
            throw new RedisConnectionException(e);
        }
    }

    public void connect() {
        if (!isConnected()) {
            try {
                socket = new Socket();
                socket.setReuseAddress(true);
                socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);
                socket.setSoLinger(true, 0);

                socket.connect(new InetSocketAddress(host, port), timeout);
                socket.setSoTimeout(timeout);
                outputStream = new RedisOutputStream(socket.getOutputStream());
                inputStream = new RedisInputStream(socket.getInputStream());
            } catch (IOException e) {
                broken = true;
                throw new RedisConnectionException(e);
            }
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
            && socket.isConnected() && !socket.isInputShutdown()
            && !socket.isOutputShutdown();
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                broken = true;
                throw new RedisConnectionException(e);
            }
        }
    }

    protected Connection sendCommand(final Protocol.Command cmd, final String... args) {
        final byte[][] bytes = new byte[args.length][];
        for (int i = 0; i < args.length; i++) {
            bytes[i] = SafeEncoder.encode(args[i]);
        }

        return sendCommand(cmd, bytes);
    }

    protected Connection sendCommand(final Protocol.Command cmd, final byte[]... args) {
        try {
            connect();

            return this;
        } catch (RedisConnectionException e) {
            broken = true;
            throw e;
        }
    }

    protected Connection sendCommand(final Protocol.Command cmd) {
        try {
            connect();

            return this;
        } catch (RedisConnectionException e) {
            broken = true;
            throw e;
        }
    }

    public void close() throws IOException {
        disconnect();
    }

    protected void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            broken = true;
            throw new RedisConnectionException(e);
        }
    }
}
