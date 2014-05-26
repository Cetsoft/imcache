/*
* Copyright (C) 2013 Cetsoft, http://www.cetsoft.com
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Library General Public
* License as published by the Free Software Foundation; either
* version 2 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Library General Public License for more details.
*
* You should have received a copy of the GNU Library General Public
* License along with this library; if not, write to the Free
* Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
* 
* Author : Yusuf Aytas
* Date   : May 24, 2014
*/
package com.cetsoft.imcache.cache.offheap;

import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.bytebuffer.Pointer;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.offheap.VersionedOffHeapCache.CacheItemSerializer;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class OffHeapCacheTest.
 */
public class VersionedOffHeapCacheTest {
	
	/** The cache loader. */
	@Mock
	CacheLoader<Object, Object> cacheLoader;
	
	/** The eviction listener. */
	@Mock
	EvictionListener<Object, Object> evictionListener;
	
	/** The index handler. */
	@Mock
	IndexHandler<Object, Object> indexHandler;
	
	/** The entry. */
	@Mock
	Entry<Object, Pointer> entry;
	
	/** The serializer. */
	@Mock
	Serializer<Object> serializer;
	
	/** The buffer store. */
	@Spy
	OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(1000, 10);
	
	/** The buffer. */
	@Mock
	OffHeapByteBuffer buffer;
	
	/** The pointer map. */
	@Mock
	ConcurrentMap<Object, Pointer> pointerMap;
	
	/** The pointer. */
	@Mock
	Pointer pointer;
	
	/** The cache. */
	VersionedOffHeapCache<Object, Object> cache;
	
	/** The random. */
	Random random = new Random();
	
	/** The value. */
	@Mock
	VersionedItem<Object> value;
	
	/** The ex value. */
	@Mock
	VersionedItem<Object> exValue;
	

	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cache = spy(new VersionedOffHeapCache<Object, Object>(bufferStore, serializer, cacheLoader, 
				evictionListener, indexHandler, 100000L, 0.5f, 1, 100000L));
		cache.offHeapCache = spy(cache.offHeapCache);
	}
	
	/**
	 * Put.
	 */
	@Test
	public void put(){
		Object object = new Object();
		doReturn(0).when(value).getVersion();
		doReturn(null).when(cache).get(object);
		doNothing().when(cache.offHeapCache).put(object, value);
		cache.put(object, value);
		verify(cache.offHeapCache).put(object, value);
	}
	
	/**
	 * Put version are not same.
	 */
	@Test(expected=StaleItemException.class)
	public void putVersionAreNotSame(){
		Object object = new Object();
		doReturn(0).when(value).getVersion();
		doReturn(1).when(exValue).getVersion();
		doReturn(exValue).when(cache).get(object);
		cache.put(object, value);
	}
	
	/**
	 * Put version race condition.
	 */
	@Test(expected=StaleItemException.class)
	public void putVersionRaceCondition(){
		Object object = new Object();
		doReturn(0).doReturn(1).when(value).getVersion();
		doReturn(null).when(cache).get(object);
		doNothing().when(cache.offHeapCache).put(object, value);
		cache.put(object, value);
	}
	
	/**
	 * Serialize cache item serializer.
	 */
	@Test
	public void serializeCacheItemSerializer(){
		Object object = new Object();
		CacheItemSerializer<Object> cacheItemSerializer = new CacheItemSerializer<Object>(serializer);
		int size = 100;
		byte[] expectedBytes = new byte[size];
		random.nextBytes(expectedBytes);
		doReturn(expectedBytes).when(serializer).serialize(object);
		doReturn(object).when(serializer).deserialize(expectedBytes);
		doReturn(object).when(value).getValue();
		doReturn(13).when(value).getVersion();
		byte[] bytes = cacheItemSerializer.serialize(value);
		VersionedItem<Object> item = cacheItemSerializer.deserialize(bytes);
		assertEquals(13, item.getVersion());
		assertEquals(object, item.getValue());
	}
}
