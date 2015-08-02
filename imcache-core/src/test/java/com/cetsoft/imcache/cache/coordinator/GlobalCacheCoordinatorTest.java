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
