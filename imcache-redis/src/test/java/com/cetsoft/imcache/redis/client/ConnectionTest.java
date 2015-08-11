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
import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.redis.client.Connection;
import com.cetsoft.imcache.redis.client.ConnectionException;
import com.cetsoft.imcache.redis.client.RedisStreamWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ConnectionTest {
	
	Connection connection;
	
	@Mock
	Socket socket;
	
	@Mock
	InetSocketAddress inetSocketAddress;
	
	@Mock
	RedisStreamWriter redisOutputStream;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		connection = spy(new Connection());
		connection.socket = socket;
		connection.streamWriter = redisOutputStream;
	}
	
	@Test
	public void open() throws ConnectionException, IOException{
		doReturn(false).when(connection).isConnected();
		doReturn(socket).when(connection).createSocket();
		connection.open();
		assertNotNull(connection.getStreamReader());
		assertNotNull(connection.getStreamWriter());
		verify(socket).setSoTimeout(Connection.DEFAULT_SOCKET_TIMEOUT);
	}
	
	@Test(expected=ConnectionException.class)
	public void openThrowsConnectionException() throws ConnectionException, IOException{
		doReturn(false).when(connection).isConnected();
		doReturn(socket).when(connection).createSocket();
		doThrow(new SocketException()).when(socket).setSoTimeout(Connection.DEFAULT_SOCKET_TIMEOUT);
		connection.open();
	}
	
	
	@Test(expected=ConnectionException.class)
	public void closeThrowsConnectionException() throws ConnectionException, IOException{
		doReturn(true).when(connection).isConnected();
		doThrow(new IOException()).when(socket).close();
		doNothing().when(redisOutputStream).flush();
		connection.close();
		verify(socket, times(2)).close();
	}
	
	@Test
	public void isConnectedReturnsFalse() throws ConnectionException, IOException{
		doReturn(false).when(socket).isBound();
		assertFalse(connection.isConnected());
	}
	
	@Test
	public void isConnectedReturnsTrue() throws ConnectionException, IOException{
		doReturn(false).when(socket).isBound();
		doReturn(false).when(socket).isClosed();
		doReturn(true).when(socket).isConnected();
		doReturn(false).when(socket).isInputShutdown();
		doReturn(false).when(socket).isOutputShutdown();
		assertFalse(connection.isConnected());
	}
}
