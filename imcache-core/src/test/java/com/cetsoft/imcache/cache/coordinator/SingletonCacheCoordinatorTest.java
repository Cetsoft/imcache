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
* Author : Christian Bourque
* Date   : August 1, 2015
*/
package com.cetsoft.imcache.cache.coordinator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.ImcacheType;
import com.cetsoft.imcache.cache.heap.HeapCache;
import com.cetsoft.imcache.cache.search.IndexHandler;

/**
 * The class SingletonCacheCoordinatorTest.
 */
public class SingletonCacheCoordinatorTest {

	/** The cache loader. */
	@Mock
	CacheLoader<Object, Object> cacheLoader;

	/** The eviction listener. */
	@Mock
	EvictionListener<Object, Object> evictionListener;

	/** The index handler. */
	@Mock
	IndexHandler<Object, Object> indexHandler;

        /** Test data. */
        private static final String KEY = "key";
        private static final String VALUE = "value";
        
        /**
         * Setup.
         */
        @Before
        public void setup() {
                MockitoAnnotations.initMocks(this);
        }

        /**
         * Get instance.
         */
        @Test
        public void getInstance() {
                SingletonCacheCoordinator coordinator1 = SingletonCacheCoordinator.getInstance();
                SingletonCacheCoordinator coordinator2 = SingletonCacheCoordinator.getInstance();

                assertEquals(coordinator1, coordinator2);
        }

        /**
         * Clear all.
         */
        @Test
        public void clearAll() {
                HeapCache<Object, Object> c1 =
                        new HeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, 100);
                c1.put(KEY, VALUE);

                HeapCache<Object, Object> c2 =
                        new HeapCache<Object, Object>(cacheLoader, evictionListener, indexHandler, 100);
                c2.put(KEY, VALUE);

                SingletonCacheCoordinator coordinator = SingletonCacheCoordinator.getInstance();
                
                coordinator.addCache(new ImcacheType(), c1);
                coordinator.addCache(new ImcacheType(), c2);

                assertEquals(VALUE, c1.get(KEY));
                assertEquals(VALUE, c2.get(KEY));

                coordinator.clearAll();

                assertNull(c1.get(KEY));
                assertNull(c2.get(KEY));
        }

}
