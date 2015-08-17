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
* Date   : May 20, 2014
*/
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;

/**
 * The Class MultiLevelCacheExample.
 */
public class MultiLevelCacheExample {

	@SuppressWarnings("null")
	public static void example() {
		final CacheDao cacheDao = null;// This is just for example purposes.
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(10000, 10);
		final Cache<String, String> offHeapCache = CacheBuilder.offHeapCache().storage(bufferStore)
				.cacheLoader(new CacheLoader<String, String>() {
					public String load(String key) {
						return cacheDao.load(key);
					}
				}).evictionListener(new EvictionListener<String, String>() {
					public void onEviction(String key, String value) {
						cacheDao.store(key, value);
					}
				}).build();
		Cache<String, String> multiLevelCache = CacheBuilder.heapCache().cacheLoader(new CacheLoader<String, String>() {
			public String load(String key) {
				return offHeapCache.get(key);
			}
		}).evictionListener(new EvictionListener<String, String>() {
			public void onEviction(String key, String value) {
				offHeapCache.put(key, value);
			}
		}).capacity(10000).build();
		multiLevelCache.put("red", "apple");
	}
	

	/**
	 * The Interface CacheDao.
	 */
	public static interface CacheDao {

		/**
		 * Load.
		 *
		 * @param key the key
		 * @return the string
		 */
		String load(String key);

		/**
		 * Store.
		 *
		 * @param key the key
		 * @param value the value
		 */
		void store(String key, String value);
	}
	
	public static void main(String[] args) {
		example();
	}
}
