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

public class HeapCacheTest {
	
	@Mock
	CacheLoader<Object, Object> cacheLoader;
	
	@Mock
	EvictionListener<Object, Object> evictionListener;
	
	@Mock
	IndexHandler<Object, Object> indexHandler;
	
	@Mock
	Map.Entry<Object, Object> entry;
	
	Map<Object, Object> limitedMap;
	
	HeapCache<Object, Object> cache;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cache = new HeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, 1000);
		cache.cache = spy(cache.cache);
		limitedMap = cache.cache;
	}
	
	@Test
	public void put(){
		doReturn(null).when(limitedMap).put(any(), any());
		doNothing().when(indexHandler).add(any(), any());
		cache.put(3, "3");
		verify(limitedMap).put(any(), any());
		verify(indexHandler).add(any(), any());
	}
	
	@Test
	public void get(){
		Object expectedObject = new Object();
		doReturn(expectedObject).when(limitedMap).get(any());
		Object actualObject = cache.get(any());
		assertEquals(expectedObject, actualObject);
	}
	
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
	
	@Test
	public void constains(){
		boolean expectedResult = true;
		doReturn(expectedResult).when(limitedMap).containsKey(any());
		Object actualResult = cache.contains(any());
		assertEquals(expectedResult, actualResult);
	}
	
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
	
	@Test
	public void hitRatio(){
		long hit = 10, miss=20;
		cache.hit = hit;
		cache.miss = miss;
		double actualHitRatio = 1/3;
		double expectedHitRatio = cache.hitRatio();
		assertEquals(expectedHitRatio, actualHitRatio, 0);
	}
	
	@Test 
	public void getLimitedMapValueInMap(){
		long hitBefore = cache.hit;
		Object expectedValue = new Object();
		limitedMap.put(expectedValue, expectedValue);
		Object actualValue = limitedMap.get(expectedValue);
		assertEquals(expectedValue, actualValue);
		assertEquals(hitBefore+1, cache.hit);
	} 
	
	@Test 
	public void getLimitedMapValueNotInMap(){
		long missBefore = cache.miss;
		Object expectedValue = new Object();
		doReturn(expectedValue).when(cacheLoader).load(expectedValue);
		Object actualValue = limitedMap.get(expectedValue);
		assertEquals(expectedValue, actualValue);
		assertEquals(missBefore+1, cache.miss);
	}
	
	@Test 
	public void removeLimitedMapValueInMap(){
		Object expectedValue = new Object();
		limitedMap.put(expectedValue, expectedValue);
		Object actualValue = limitedMap.remove(expectedValue);
		assertEquals(expectedValue, actualValue);
		verify(evictionListener).onEviction(expectedValue, expectedValue);
	} 
	
	@Test 
	public void removeLimitedMapValueNotInMap(){
		Object object = new Object();
		Object actualValue = limitedMap.remove(object);
		assertEquals(null, actualValue);
	}
	
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
