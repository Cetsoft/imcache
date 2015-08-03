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
* Date   : Nov 15, 2013
*/
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheType;
import com.cetsoft.imcache.cache.ImcacheType;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.coordinator.CacheCoordinator;
import com.cetsoft.imcache.cache.coordinator.CacheFactory;
import com.cetsoft.imcache.cache.coordinator.LocalCacheCoordinator;

/**
 * The Class CacheCoordinatorExample.
 */
public class CacheCoordinatorExample {

	/** The str cache type. */
	private static CacheType<String, String> strCacheType = new ImcacheType<String, String>();

	/** The factory. */
	private static CacheFactory factory = new CacheFactory() {
		public <K, V> Cache<K, V> create() {
			return CacheBuilder.heapCache().build();
		}
	};

	public static void example() {
		final CacheCoordinator cacheCoordinator = new LocalCacheCoordinator(factory);
		final CacheTypeTest test = new CacheTypeTest(cacheCoordinator);
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				public void run() {
					test.setValue(strCacheType, Thread.currentThread().getName(), Thread.currentThread().getName());
					System.out.println(test.getValue(strCacheType, Thread.currentThread().getName()));
				}
			}, "Thread #" + i).start();
		}
	}

	/**
	 * The Class CacheTypeTest.
	 */
	private static class CacheTypeTest {

		/** The cache coordinator. */
		final CacheCoordinator cacheCoordinator;

		/**
		 * Instantiates a new cache type test.
		 *
		 * @param cacheCoordinator the cache coordinator
		 */
		public CacheTypeTest(CacheCoordinator cacheCoordinator) {
			this.cacheCoordinator = cacheCoordinator;
		}

		/**
		 * Sets the value.
		 *
		 * @param type the type
		 * @param key the key
		 * @param value the value
		 */
		public void setValue(CacheType<String, String> type, String key, String value) {
			cacheCoordinator.getCache(type).put(key, value);
		}

		/**
		 * Gets the value.
		 *
		 * @param type the type
		 * @param key the key
		 * @return the value
		 */
		public String getValue(CacheType<String, String> type, String key) {
			return cacheCoordinator.getCache(type).get(key);
		}
	}
	
	public static void main(String[] args) {
		example();
	}

}
