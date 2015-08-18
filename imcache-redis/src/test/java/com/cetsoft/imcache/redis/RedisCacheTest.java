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
* Date   : Aug 14, 2015
*/
package com.cetsoft.imcache.redis;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.redis.client.Client;
import com.cetsoft.imcache.redis.client.ConnectionException;
import com.cetsoft.imcache.serialization.Serializer;


public class RedisCacheTest {

	@Mock
	Client client;
	
	//This serializes just integers for testing purposes
	Serializer<Object> serializer = new Serializer<Object>() {
		
		@Override
		public byte[] serialize(Object value) {
			int shortValue = (Integer) value;
			byte[] bytes = new byte[]{(byte) shortValue};
			return bytes;
		}
		
		@Override
		public Object deserialize(byte[] payload) {
			if(payload == null){
				return null;
			}
			return (int)(payload[0]);
		}
	};
	
	@Mock
	CacheLoader<Integer, Integer> cacheLoader;
	
	@Mock
	EvictionListener<Integer, Integer> evictionListener;
	
	RedisCache<Integer, Integer> cache;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cache = new RedisCache<Integer, Integer>(cacheLoader, evictionListener, serializer, client);
	}
	
	@Test
	public void size() throws ConnectionException, IOException{
		int size = 3;
		doReturn(size).when(client).dbsize();
		int actualSize = cache.size();
		assertEquals(size, actualSize);
	}
	
	@Test(expected=RedisCacheException.class)
	public void sizeThrowConnectionException() throws ConnectionException, IOException{
		doThrow(new ConnectionException("")).when(client).dbsize();
		cache.size();
	}
	
	@Test(expected=RedisCacheException.class)
	public void sizeThrowIOException() throws ConnectionException, IOException{
		doThrow(new IOException("")).when(client).dbsize();
		cache.size();
	}
	
	@Test
	public void clear() throws ConnectionException, IOException{
		cache.clear();
		verify(client).flushdb();
	}
	
	@Test(expected=RedisCacheException.class)
	public void clearThrowConnectionException() throws ConnectionException, IOException{
		doThrow(new ConnectionException("")).when(client).flushdb();
		cache.clear();
	}
	
	@Test(expected=RedisCacheException.class)
	public void clearThrowIOException() throws ConnectionException, IOException{
		doThrow(new IOException("")).when(client).flushdb();
		cache.clear();
	}
	
	
	@Test
	public void put() throws ConnectionException, IOException{
		int key = 3;
		int value = 5;
		cache.put(key, value);
		verify(client).set(serializer.serialize(key), serializer.serialize(value));
	}
	
	@Test(expected=RedisCacheException.class)
	public void putConnectionException() throws ConnectionException, IOException{
		doThrow(new ConnectionException("")).when(client).set((byte[])any(), (byte[])any());
		cache.put(3, 3);
	}
	
	@Test(expected=RedisCacheException.class)
	public void putThrowIOException() throws ConnectionException, IOException{
		doThrow(new IOException("")).when(client).set((byte[])any(), (byte[])any());
		cache.put(3, 3);
	}
	
	@Test
	public void invalidate() throws ConnectionException, IOException{
		int key = 3;
		int value = 5;
		doReturn(serializer.serialize(value)).when(client).expire(serializer.serialize(key));
		int actualValue = cache.invalidate(key);
		assertEquals(value, actualValue);
		verify(evictionListener).onEviction(key, value);
	}
	
	@Test(expected=RedisCacheException.class)
	public void invalidateConnectionException() throws ConnectionException, IOException{
		int key = 3;
		doThrow(new ConnectionException("")).when(client).expire(serializer.serialize(key));
		cache.invalidate(key);
	}
	
	@Test(expected=RedisCacheException.class)
	public void invalidateThrowIOException() throws ConnectionException, IOException{
		int key = 3;
		doThrow(new IOException("")).when(client).expire(serializer.serialize(key));
		cache.invalidate(key);
	}
	
	@Test
	public void get() throws ConnectionException, IOException{
		int key = 3;
		int value = 5;
		doReturn(serializer.serialize(value)).when(client).get(serializer.serialize(key));
		int actualValue = cache.get(key);
		assertEquals(value, actualValue);
	}
	
	@Test
	public void getLoadsValueFromLoader() throws ConnectionException, IOException{
		int key = 3;
		int value = 5;
		doReturn(null).when(client).get(serializer.serialize(key));
		doReturn(value).when(cacheLoader).load(key);
		int actualValue = cache.get(key);
		assertEquals(value, actualValue);
		verify(client).set(serializer.serialize(key), serializer.serialize(value));
	}
	
	@Test(expected=RedisCacheException.class)
	public void getThrowConnectionException() throws ConnectionException, IOException{
		int key = 3;
		doThrow(new ConnectionException("")).when(client).get(serializer.serialize(key));
		cache.get(key);
	}
	
	@Test(expected=RedisCacheException.class)
	public void getThrowIOException() throws ConnectionException, IOException{
		int key = 3;
		doThrow(new IOException("")).when(client).get(serializer.serialize(key));
		cache.get(key);
	}
	
	public void hitRatio() throws ConnectionException, IOException{
		int key = 3;
		int value = 5;
		doReturn(null).doReturn(serializer.serialize(value)).when(client).get(serializer.serialize(key));
		doReturn(value).when(cacheLoader).load(key);
		cache.get(key);
		cache.get(key);
		assertEquals(0.5, cache.hitRatio(), 0.0001);
	}
	
}
