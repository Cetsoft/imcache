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
* Date   : Aug 17, 2015
*/
package com.cetsoft.imcache.redis.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MultiRedisClientTest {
	
	byte[] key = {'0'};
	byte[] value = {'0'};
	
	@Mock
	Client client;
	
	MultiRedisClient redisClient;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		redisClient = spy(new MultiRedisClient("", 0));
		doReturn(client).when(redisClient).getClient();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void redisClientThrowsIllegalArgument(){
		new MultiRedisClient("", 1, 0);
	}
	
	@Test
	public void ping() throws ConnectionException, IOException{
		redisClient.ping();
		verify(client).ping();
	}
	
	@Test
	public void set() throws ConnectionException, IOException{
		redisClient.set(key, value);
		verify(client).set(key, value);
	}
	
	@Test
	public void get() throws ConnectionException, IOException{
		doReturn(value).when(client).get(key);
		byte[] actualValue = redisClient.get(key);
		assertEquals(value, actualValue);
	}
	
	@Test
	public void expire() throws ConnectionException, IOException{
		doReturn(value).when(client).expire(key);
		byte[] actualValue = redisClient.expire(key);
		assertEquals(value, actualValue);
	}
	
	@Test
	public void flushdb() throws ConnectionException, IOException{
		redisClient.flushdb();
		verify(client).flushdb();
	}
	
	@Test
	public void dbsize() throws ConnectionException, IOException{
		int size = 5;
		doReturn(size).when(client).dbsize();
		int actualSize = redisClient.dbsize();
		assertEquals(size, actualSize);
	}
	
	@Test
	public void getClient() throws ConnectionException, IOException{
		MultiRedisClient redisClient = new MultiRedisClient("", 2);
		Client firstClient = redisClient.getClient();
		redisClient.getClient();
		redisClient.getClient();
		assertEquals(firstClient, redisClient.getClient());
	}
	
}
