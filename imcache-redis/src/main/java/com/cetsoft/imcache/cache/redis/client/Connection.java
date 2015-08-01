/*
* Copyright (C) 2015 Cetsoft, http://www.cetsoft.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* 
* Author : Yusuf Aytas
* Date   : Sep 8, 2014
*/
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

/**
 * The Class Connection which will instantiate to the redis instance.
 */
public class Connection implements Closeable {

    /** The host. */
    private String host;
    
    /** The port. */
    private int port = Protocol.DEFAULT_PORT;
    
    /** The timeout. */
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    
    /** The socket. */
    private Socket socket;
    
    /** The output stream. */
    private RedisOutputStream outputStream;
    
    /** The input stream. */
    private RedisInputStream inputStream;

    private boolean broken = false;

    /**
     * Instantiates a new connection.
     */
    public Connection() {

    }

    /**
     * Instantiates a new connection.
     *
     * @param host the host
     */
    public Connection(String host) {
        super();
        this.host = host;
    }

    /**
     * Instantiates a new connection.
     *
     * @param host the host
     * @param port the port
     */
    public Connection(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     *
     * @param host the new host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the port.
     *
     * @param port the new port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the socket.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Gets the timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout.
     *
     * @param timeout the new timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Sets the timeout infinite.
     */
    public void setTimeoutInfinite() {
        try {
            if (!isConnected())
                connect();
            socket.setKeepAlive(true);
            socket.setSoTimeout(0);
        } catch (SocketException e) {
            throw new RedisConnectionException(e);
        }
    }

    /**
     * Rollback timeout.
     *
     * @param timeout the timeout
     */
    public void rollbackTimeout(int timeout) {
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout(timeout);
        } catch (SocketException e) {
            throw new RedisConnectionException(e);
        }
    }

    /**
     * Connect.
     */
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
                throw new RedisConnectionException(e);
            }
        }
    }

    /**
     * Checks if is connected.
     *
     * @return true, if is connected
     */
    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
            && socket.isConnected() && !socket.isInputShutdown()
            && !socket.isOutputShutdown();
    }

    /**
     * Disconnect.
     */
    public void disconnect() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RedisConnectionException(e);
            }
        }
    }

    /**
     * Send command.
     *
     * @param cmd the cmd
     * @param args the args
     * @return the connection
     */
    protected Connection sendCommand(final Protocol.Command cmd, final String... args) {
        final byte[][] bytes = new byte[args.length][];
        for (int i = 0; i < args.length; i++) {
            bytes[i] = SafeEncoder.encode(args[i]);
        }

        return sendCommand(cmd, bytes);
    }

    /**
     * Send command.
     *
     * @param cmd the cmd
     * @param args the args
     * @return the connection
     */
    protected Connection sendCommand(final Protocol.Command cmd, final byte[]... args) {
        try {
            connect();
            Protocol.sendCommand(outputStream, cmd, args);
            return this;
        } catch (RedisConnectionException e) {
            broken = true;
            throw e;
        }
    }

    /**
     * Send command.
     *
     * @param cmd the cmd
     * @return the connection
     */
    protected Connection sendCommand(final Protocol.Command cmd) {
        try {
            connect();
            Protocol.sendCommand(outputStream, cmd, new byte[0][]);
            return this;
        } catch (RedisConnectionException e) {
            broken = true;
            throw e;
        }
    }

    protected String getStatusCodeReply() {
        flush();
        final byte[] resp = (byte[]) readProtocolWithCheckingBroken();
        if (resp == null) {
            return null;
        } else {
            return SafeEncoder.encode(resp);
        }
    }

    public byte[] getBinaryBulkReply() {
        flush();
        return (byte[]) readProtocolWithCheckingBroken();
    }

    protected Object readProtocolWithCheckingBroken() {
        try {
            return Protocol.read(inputStream);
        } catch (RedisConnectionException ex) {
            broken = true;
            throw ex;
        }
    }

    /* (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    public void close() throws IOException {
        disconnect();
    }

    /**
     * Flush.
     */
    protected void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            broken = true;
            throw new RedisConnectionException(e);
        }
    }
}
