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
* Date   : May 21, 2014
*/
package com.cetsoft.imcache.heap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.heap.ConcurrentHeapCache;

/**
 * The Class ConcurrentHeapCacheTest.
 */
public class ConcurrentHeapCacheTest {
	
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
	Map.Entry<Object, Object> entry;
	
	/** The limited map. */
	Map<Object, Object> limitedMap;
	
	/** The cache. */
	ConcurrentHeapCache<Object, Object> cache;

	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cache = new ConcurrentHeapCache<Object,Object>(cacheLoader, evictionListener, indexHandler, 1000);
		cache.cache = spy(cache.cache);
		limitedMap = cache.cache;
	}
	
	/**
	 * Put.
	 */
	@Test
	public void put(){
		doReturn(null).when(limitedMap).put(any(), any());
		doNothing().when(indexHandler).add(any(), any());
		cache.put(3, "3");
		verify(limitedMap).put(any(), any());
	}
	
	/**
	 * Gets the.
	 */
	@Test
	public void get(){
		Object expectedObject = new Object();
		doReturn(expectedObject).when(limitedMap).get(any());
		Object actualObject = cache.get(any());
		assertEquals(expectedObject, actualObject);
	}
	
	/**
	 * Invalidate.
	 */
	@Test
	public void invalidate(){
		Object expectedValue = new Object();
		doReturn(expectedValue).when(limitedMap).remove(any());
		Object actualValue = cache.invalidate(any());
		verify(limitedMap).remove(any());
		assertEquals(expectedValue, actualValue);
	}
	
	/**
	 * Constains.
	 */
	@Test
	public void constains(){
		boolean expectedResult = true;
		doReturn(expectedResult).when(limitedMap).containsKey(any());
		Object actualResult = cache.contains(any());
		assertEquals(expectedResult, actualResult);
	}
	
	/**
	 * Clear.
	 */
	@Test
	public void clear(){
		Set<Object> keySet = new HashSet<Object>();
		keySet.add(1);
		keySet.add(2);
		doReturn(keySet).when(limitedMap).keySet();
		doNothing().when(indexHandler).clear();
		cache.clear();
		verify(limitedMap, times(2)).remove(any());
	}
	
	/**
	 * Hit ratio.
	 */
	@Test
	public void hitRatio(){
		long hit = 10, miss=20;
		cache.hit.set(hit);
		cache.miss.set(miss);
		double actualHitRatio = 1/3.0;
		double expectedHitRatio = cache.hitRatio();
		assertEquals(expectedHitRatio, actualHitRatio, 0);
	}
	
    /**
     * Size.
     */
    @Test
    public void size() {
            cache.put(1, new Object());
            cache.put(2, new Object());
            cache.put(3, new Object());                
            assertEquals(3, cache.size());            
    }
        
	/**
	 * Gets the limited map value in map.
	 *
	 * @return the limited map value in map
	 */
	@Test 
	public void getLimitedMapValueInMap(){
		Object expectedValue = new Object();
		cache = new ConcurrentHeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, 10);
		cache.cache.put(expectedValue, expectedValue);
		Object actualValue = cache.get(expectedValue);
		assertEquals(expectedValue, actualValue);
	} 
	
	/**
	 * Gets the limited map value not in map.
	 *
	 * @return the limited map value not in map
	 */
	@Test 
	public void getLimitedMapValueNotInMap(){
		long missBefore = cache.miss.get();
		Object expectedValue = new Object();
		doReturn(null).when(limitedMap).put(any(), any());
		doReturn(expectedValue).when(cacheLoader).load(expectedValue);
		Object actualValue = cache.get(expectedValue);
		assertEquals(expectedValue, actualValue);
		assertEquals(missBefore+1, cache.miss.get());
		verify(limitedMap).put(any(), any());
	}
	
	/**
	 * Put concurrent limited hash map size greater than capacity.
	 */
	@Test
	public void putConcurrentLimitedHashMapSizeGreaterThanCapacity(){
		cache = new ConcurrentHeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, 0);
		Object object = new Object();
		cache.cache.put(object, object);
		doReturn(object).when(entry).getKey();
		doReturn(object).when(entry).getValue();
		doNothing().when(evictionListener).onEviction(object, object);
		doNothing().when(indexHandler).add(object, object);
		cache.put(object, object);
		verify(indexHandler,times(2)).add(object, object);
	}
	
	/**
	 * Removes the concurrent limited hash map exist ex key.
	 */
	@Test
	public void removeConcurrentLimitedHashMapExistExKey(){
		cache = new ConcurrentHeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, 0);
		Object object = new Object();
		cache.cache.put(object, object);
		doReturn(object).when(entry).getKey();
		doReturn(object).when(entry).getValue();
		doNothing().when(evictionListener).onEviction(object, object);
		doNothing().when(indexHandler).add(object, object);
		cache.invalidate(object);
		verify(indexHandler).add(object, object);
	}
}
