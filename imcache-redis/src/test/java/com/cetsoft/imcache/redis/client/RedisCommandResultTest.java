/*
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
 * Date   : Aug 12, 2015
 */
package com.cetsoft.imcache.redis.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class RedisCommandResultTest {

	@Mock
	Connection connection;

	@Mock
	InputStream inputStream;

	RedisStreamReader streamReader;

	RedisCommandResult commandResult;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		streamReader = spy(new RedisStreamReader(inputStream));
		commandResult = spy(new RedisCommandResult(connection));
		doReturn(streamReader).when(connection).getStreamReader();
	}

	@Test
	public void getInt() throws ConnectionException, IOException {
		int value = 10;
		doReturn(value).when(streamReader).readInt();
		doReturn(RedisBytes.COLON_BYTE).when(streamReader).readByte();
		int actualValue = commandResult.getInt();
		assertEquals(value, actualValue);
	}
	
	@Test(expected=ConnectionException.class)
	public void getIntThrowsConnectionException() throws ConnectionException, IOException {
		int value = 10;
		doReturn(value).when(streamReader).readInt();
		doReturn(RedisBytes.ASTERISK_BYTE).when(streamReader).readByte();
		commandResult.getInt();
	}
	
	@Test
	public void getStatus() throws ConnectionException, IOException {
		String status = "OK";
		doReturn(status).when(streamReader).readString();
		doReturn(RedisBytes.PLUS_BYTE).when(streamReader).readByte();
		String actualStatus = commandResult.getStatus();
		assertEquals(status, actualStatus);
	}
	
	@Test
	public void getBytes() throws ConnectionException, IOException {
		int length = 3;
		byte[] bytes = {1, 2, 3};
		doReturn(RedisBytes.DOLLAR_BYTE).when(streamReader).readByte();
		doReturn(length).when(streamReader).readInt();
		doReturn(bytes).when(streamReader).read(length);
		byte[] actualBytes = commandResult.getBytes();
		assertArrayEquals(bytes, actualBytes);
	}

}
