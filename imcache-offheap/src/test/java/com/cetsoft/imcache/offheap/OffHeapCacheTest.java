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
* Date   : May 24, 2014
*/
package com.cetsoft.imcache.offheap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.offheap.OffHeapCache;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.offheap.bytebuffer.Pointer;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class OffHeapCacheTest.
 */
public class OffHeapCacheTest {
	
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
	OffHeapCache<Object, Object> cache;
	
	/** The random. */
	Random random = new Random();

	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cache = spy(new OffHeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, bufferStore, 
				serializer, 100000000, 05f, 1, 100000000));
		cache.pointerMap = pointerMap;
	}
	
	/**
	 * Put.
	 */
	@Test
	public void put(){
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Object object = new Object();
		doReturn(null).when(pointerMap).get(object);
		doReturn(pointer).when(bufferStore).store(bytes);
		doReturn(bytes).when(serializer).serialize(object);
		doReturn(null).when(pointerMap).put(object, pointer);
		cache.put(object, object);
		verify(pointerMap).put(object, pointer);
	}
	
	/**
	 * Put when exist.
	 */
	@Test
	public void putWhenExist(){
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Object object = new Object();
		doReturn(pointer).when(pointerMap).get(object);
		doReturn(pointer).when(bufferStore).update(pointer, bytes);
		doReturn(bytes).when(serializer).serialize(object);
		doReturn(null).when(pointerMap).put(object, pointer);
		cache.put(object, object);
		verify(pointerMap).put(object, pointer);
	}
	
	/**
	 * Gets the.
	 */
	@Test
	public void get(){
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Object object = new Object();
		doReturn(pointer).when(pointerMap).get(object);
		doReturn(bytes).when(bufferStore).retrieve(pointer);
		doReturn(object).when(serializer).deserialize(bytes);
		Object actualObject = cache.get(object);
		assertEquals(object, actualObject);
	}
	
	/**
	 * Gets the loaded.
	 *
	 * @return the loaded
	 */
	@Test
	public void getLoaded(){
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Object object = new Object();
		doReturn(null).when(pointerMap).get(object);
		doReturn(object).when(cacheLoader).load(object);
		doNothing().when(cache).put(object, object);
		Object actualObject = cache.get(object);
		assertEquals(object, actualObject);
	}
	
	/**
	 * Invalidate.
	 */
	@Test
	public void invalidate(){
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Object object = new Object();
		doReturn(pointer).when(pointerMap).get(object);
		doReturn(bytes).when(bufferStore).remove(pointer);
		doReturn(object).when(serializer).deserialize(bytes);
		Object actualObject = cache.invalidate(object);
		assertEquals(object, actualObject);
	}
	
	/**
	 * Contains.
	 */
	@Test
	public void contains(){
		Object object = new Object();
		doReturn(true).when(pointerMap).containsKey(object);
		boolean result = cache.contains(object);
		assertTrue(result);
	}
	
	/**
	 * Clear.
	 */
	@Test
	public void clear(){
		doNothing().when(pointerMap).clear();
		doNothing().when(bufferStore).free();
		cache.clear();
		verify(pointerMap).clear();
		verify(bufferStore).free();
	}

        /**
         * Size.
         */
        @Test
        public void size() {
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Object object = new Object();
		doReturn(null).when(pointerMap).get(object);
		doReturn(pointer).when(bufferStore).store(bytes);
		doReturn(bytes).when(serializer).serialize(object);
		doReturn(null).when(pointerMap).put(object, pointer);
                doReturn(1).when(pointerMap).size();
		cache.put(object, object);
		verify(pointerMap).put(object, pointer);
                assertEquals(1, cache.size());
                verify(pointerMap).size();
        }
        
	/**
	 * Do eviction.
	 */
	@Test
	public void doEviction(){
		Set<Entry<Object,Pointer>> entries = new HashSet<Entry<Object,Pointer>>();
		entries.add(entry);
		Object object = new Object();
		doReturn(entries).when(pointerMap).entrySet();
		doReturn(pointer).when(entry).getValue();
		doReturn(object).when(entry).getKey();
		doReturn(object).when(cache).invalidate(object);
		doReturn(-1000L).when(pointer).getAccessTime();
		cache.doEviction(-1110000L);
		verify(cache).invalidate(object);
	}
	
	/**
	 * Clean buffers.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void cleanBuffers(){
		float bufferCleanerThreshold = 0.5f;
		Collection<Pointer> values = new ArrayList<Pointer>();
		values.add(pointer);
		doReturn(values).when(pointerMap).values();
		doReturn(buffer).when(pointer).getOffHeapByteBuffer();
		doReturn(0).when(buffer).getIndex();
		doReturn(bufferCleanerThreshold+0.1f).when(cache).getDirtyRatio(pointer);
		doNothing().when(bufferStore).redistribute(anyList());
		doNothing().when(bufferStore).free(0);
		cache.cleanBuffers(bufferCleanerThreshold);
		verify(cache).getDirtyRatio(pointer);
		verify(bufferStore).free(0);
		verify(bufferStore).redistribute(anyList());
	}
	
	
	/**
	 * Gets the dirty ratio.
	 *
	 * @return the dirty ratio
	 */
	public void getDirtyRatio(){
		doReturn(buffer).when(pointer).getOffHeapByteBuffer();
		doReturn(50).when(buffer).dirtyMemory();
		doReturn(50).when(buffer).freeMemory();
		doReturn(50).when(buffer).usedMemory();
		float actualDirtyRatio = cache.getDirtyRatio(pointer);
		assertEquals(1/3, actualDirtyRatio, 0.001f);
	}
	
	/**
	 * Inits the throws exception.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void initThrowsException(){
		cache.initCache(bufferStore, serializer, 0, 0, -12, 0);
	}
	
	/**
	 * Inits the.
	 */
	@Test
	public void init(){
		doNothing().when(cache).doEviction(1);
		doNothing().when(cache).cleanBuffers(1);
		cache.initCache(bufferStore, serializer, 1, 1, 1, 1);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
		verify(cache, atLeast(1)).cleanBuffers(1);
		verify(cache, atLeast(1)).doEviction(1);
	}
}
