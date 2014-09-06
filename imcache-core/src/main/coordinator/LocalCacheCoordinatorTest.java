/*
* Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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
* Date   : May 25, 2014
*/
package com.cetsoft.imcache.cache.coordinator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheType;

/**
 * The Class LocalCacheCoordinatorTest.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LocalCacheCoordinatorTest {

	/** The cache. */
	@Mock
	Cache cache;
	
	/** The type. */
	@Mock
	CacheType type;
	
	/** The cache map. */
	@Mock
	Map<Integer, Cache> cacheMap;
	
	/** The cache factory. */
	@Mock
	CacheFactory cacheFactory;
	
	/** The cache coordinator. */
	@Spy
	LocalCacheCoordinator cacheCoordinator = new LocalCacheCoordinator();
	
	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cacheCoordinator.cacheFactory = cacheFactory;
	}
	
	/**
	 * Gets the cache.
	 *
	 * @return the cache
	 */
	@Test
	public void getCache(){
		doReturn(1).when(type).getType();
		doReturn(cacheMap).when(cacheCoordinator).getOrCreate();
		doReturn(cache).when(cacheMap).get(anyInt());
		Cache actualCache = cacheCoordinator.getCache(type);
		assertEquals(cache, actualCache);
	}
	
	/**
	 * Gets the cache creates cache.
	 *
	 * @return the cache creates cache
	 */
	@Test
	public void getCacheCreatesCache(){
		doReturn(1).when(type).getType();
		doReturn(cacheMap).when(cacheCoordinator).getOrCreate();
		doReturn(null).when(cacheMap).get(anyInt());
		doReturn(cache).when(cacheFactory).create();
		doNothing().when(cacheCoordinator).addCache(type, cache);
		Cache actualCache = cacheCoordinator.getCache(type);
		verify(cacheCoordinator).addCache(type, cache);
		assertEquals(cache, actualCache);
	}
	
	/**
	 * Adds the cache.
	 */
	@Test
	public void addCache(){
		doReturn(1).when(type).getType();
		doReturn(cacheMap).when(cacheCoordinator).getOrCreate();
		doReturn(null).when(cacheMap).put(1, cache);
		cacheCoordinator.addCache(type, cache);
		verify(cacheCoordinator).getOrCreate();
		verify(cacheMap).put(1, cache);
	}
	
	/**
	 * Gets the or create.
	 *
	 * @return the or create
	 */
	@Test
	public void getOrCreate(){
		Map<Integer, Cache> expectedMap = cacheCoordinator.getOrCreate();
		Map<Integer, Cache> actualMap = cacheCoordinator.getOrCreate();
		assertEquals(expectedMap, actualMap);
	}
}
