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
* Date   : May 21, 2014
*/
package com.cetsoft.imcache.cache.heap;

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
		double actualHitRatio = 1/3;
		double expectedHitRatio = cache.hitRatio();
		assertEquals(expectedHitRatio, actualHitRatio, 0);
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
		verify(evictionListener).onEviction(object, object);
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
		verify(evictionListener).onEviction(object, object);
		verify(indexHandler).add(object, object);
	}
}