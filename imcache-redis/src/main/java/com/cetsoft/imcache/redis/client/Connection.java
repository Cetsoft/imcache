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
* Date   : Aug 5, 2015
*/
package com.cetsoft.imcache.redis.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The Class Connection.
 */
public class Connection {

	/** The Constant DEFAULT_TIMEOUT. */
	public final static int DEFAULT_TIMEOUT = 100;
	
	/** The Constant DEFAULT_SOCKET_TIMEOUT. */
	public final static int DEFAULT_SOCKET_TIMEOUT = 100;
	
	/** The Constant DEFAULT_HOST. */
	public static final String DEFAULT_HOST = "localhost";
	
	/** The Constant DEFAULT_PORT. */
	public static final int DEFAULT_PORT = 6379;
	
	/** The host. */
	private String host;
	
	/** The port. */
	private int port;
	
	/** The timeout. */
	private int timeout;
	
	/** The socket timeout. */
	private int socketTimeout;
	
	/** The socket. */
	Socket socket;
	
	/** The stream writer. */
	RedisStreamWriter streamWriter;
	
	/** The stream reader. */
	RedisStreamReader streamReader;

	/**
	 * Instantiates a new connection.
	 */
	public Connection() {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}
	
	/**
	 * Instantiates a new connection.
	 *
	 * @param host the host
	 */
	public Connection(final String host) {
		this(host, DEFAULT_PORT);
	}

	/**
	 * Instantiates a new connection.
	 *
	 * @param host the host
	 * @param port the port
	 */
	public Connection(final String host, final int port) {
		this.host = host;
		this.port = port;
		this.timeout = DEFAULT_TIMEOUT;
		this.socketTimeout = DEFAULT_SOCKET_TIMEOUT;
	}

	/**
	 * Gets the timeout.
	 *
	 * @return the timeout
	 */
	public long getTimeout() {
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
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
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
	 * Checks if is connected.
	 *
	 * @return true, if is connected
	 */
	public boolean isConnected() {
		return socket != null && socket.isBound() && !socket.isClosed() && socket.isConnected()
				&& !socket.isInputShutdown() && !socket.isOutputShutdown();
	}

	/**
	 * Opens a new connection.
	 *
	 * @throws ConnectionException the connection exception
	 */
	public void open() throws ConnectionException {
		if (!isConnected()) {
			try {
				socket = createSocket();
				socket.setReuseAddress(true);
				// Monitor TCP connection.
				socket.setKeepAlive(true);
				// Make sure packets arrive timely.
				socket.setTcpNoDelay(true);
				socket.setSoLinger(true, 0);

				socket.connect(getInetSocketAddress(), timeout);
				socket.setSoTimeout(socketTimeout);
				streamWriter = new RedisStreamWriter(socket.getOutputStream());
				streamReader = new RedisStreamReader(socket.getInputStream());
			} catch (IOException ex) {
				throw new ConnectionException(ex);
			}
		}
	}

	/**
	 * Creates the socket.
	 *
	 * @return the socket
	 */
	protected Socket createSocket() {
		return new Socket();
	}

	/**
	 * Gets the inet socket address.
	 *
	 * @return the inet socket address
	 */
	protected InetSocketAddress getInetSocketAddress() {
		return new InetSocketAddress(host, port);
	}

	/**
	 * Close the connection.
	 *
	 * @throws ConnectionException the connection exception
	 */
	public void close() throws ConnectionException {
		if (isConnected()) {
			try {
				streamWriter.flush();
				socket.close();
			} catch (IOException ex) {
				throw new ConnectionException(ex);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					//ignore exception.
				}
			}
		}
	}
	
	/**
	 * Gets the stream writer.
	 *
	 * @return the output stream
	 */
	public RedisStreamWriter getStreamWriter() {
		return streamWriter;
	}

	/**
	 * Gets the stream reader.
	 *
	 * @return the input stream
	 */
	public RedisStreamReader getStreamReader() {
		return streamReader;
	}

}
