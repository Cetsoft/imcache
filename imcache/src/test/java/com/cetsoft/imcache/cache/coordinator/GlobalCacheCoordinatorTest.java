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
* Date   : May 25, 2014
*/
package com.cetsoft.imcache.cache.coordinator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheType;

/**
 * The Class GlobalCacheCoordinatorTest.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GlobalCacheCoordinatorTest {

	/** The cache. */
	@Mock
	Cache cache;
	
	/** The cache type. */
	@Mock
	CacheType cacheType;
	
	/** The cache coordinator. */
	@Spy
	GlobalCacheCoordinator cacheCoordinator = new GlobalCacheCoordinator();
	
	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Gets the cache.
	 *
	 * @return the cache
	 */
	@Test
	public void getCache(){
		final CountDownLatch latch = new CountDownLatch(1);
		doReturn(1).when(cacheType).getType();
		new Thread(new Runnable() {
			public void run() {
				cacheCoordinator.addCache(cacheType, cache);
				latch.countDown();
			}
		}).start();
		try {
			latch.await();
		} catch (InterruptedException e) {}
		Cache actualCache = cacheCoordinator.getCache(cacheType);
		assertEquals(cache, actualCache);
	}
}
