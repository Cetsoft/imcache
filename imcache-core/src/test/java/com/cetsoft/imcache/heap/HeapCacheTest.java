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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.heap.HeapCache;

/**
 * The Class HeapCacheTest.
 */
public class HeapCacheTest {
	
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
	HeapCache<Object, Object> cache;

	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cache = new HeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, 1000);
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
		verify(indexHandler).add(any(), any());
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
		doNothing().when(indexHandler).remove(any(), any());
		Object actualValue = cache.invalidate(any());
		verify(limitedMap).remove(any());
		verify(indexHandler).remove(any(), any());
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
		limitedMap.put(1, new Object());
		limitedMap.put(2, new Object());
		doNothing().when(indexHandler).clear();
		cache.clear();
		verify(limitedMap, times(2)).remove(any());
		verify(indexHandler).clear();
	}
	
	/**
	 * Hit ratio.
	 */
	@Test
	public void hitRatio(){
		long hit = 10, miss=20;
		cache.hit = hit;
		cache.miss = miss;
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
		long hitBefore = cache.hit;
		Object expectedValue = new Object();
		limitedMap.put(expectedValue, expectedValue);
		Object actualValue = limitedMap.get(expectedValue);
		assertEquals(expectedValue, actualValue);
		assertEquals(hitBefore+1, cache.hit);
	} 
	
	/**
	 * Gets the limited map value not in map.
	 *
	 * @return the limited map value not in map
	 */
	@Test 
	public void getLimitedMapValueNotInMap(){
		long missBefore = cache.miss;
		Object expectedValue = new Object();
		doReturn(null).when(limitedMap).put(any(), any());
		doReturn(expectedValue).when(cacheLoader).load(expectedValue);
		Object actualValue = limitedMap.get(expectedValue);
		assertEquals(expectedValue, actualValue);
		assertEquals(missBefore+1, cache.miss);
		verify(limitedMap).put(any(), any());
	}
	
	/**
	 * Removes the limited map value in map.
	 */
	@Test 
	public void removeLimitedMapValueInMap(){
		Object expectedValue = new Object();
		limitedMap.put(expectedValue, expectedValue);
		Object actualValue = limitedMap.remove(expectedValue);
		assertEquals(expectedValue, actualValue);
		verify(evictionListener).onEviction(expectedValue, expectedValue);
	} 
	
	/**
	 * Removes the limited map value not in map.
	 */
	@Test 
	public void removeLimitedMapValueNotInMap(){
		Object object = new Object();
		Object actualValue = limitedMap.remove(object);
		assertEquals(null, actualValue);
	}
	
	/**
	 * Removes the eldest entry limited map capacity not enough.
	 */
	@Test 
	public void removeEldestEntryLimitedMapCapacityNotEnough(){
		Object object = new Object();
		doReturn(object).when(entry).getKey();
		doReturn(object).when(entry).getValue();
		doReturn(1000000).when(limitedMap).size();
		limitedMap.put(object, object);
		verify(evictionListener).onEviction(object, object);
	}
}
