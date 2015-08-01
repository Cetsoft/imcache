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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.cache.coordinator;

import java.util.HashMap;
import java.util.Map;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheType;

/**
 * The Class LocalCacheCoordinator provides local caching for the threads.
 * This coordinator can only be used for non-concurrent caching.
 */
@SuppressWarnings("rawtypes")
public class LocalCacheCoordinator implements CacheCoordinator {

	CacheFactory cacheFactory;

	public LocalCacheCoordinator() {
	}

	public LocalCacheCoordinator(CacheFactory cacheFactory) {
		this.cacheFactory = cacheFactory;
	}

	/** The cache map thread local. */
	ThreadLocal<Map<Integer, Cache>> cacheMapThreadLocal = new ThreadLocal<Map<Integer, Cache>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.coordinator.CacheCoordinator#getCache(com.cetsoft
	 * .imcache.cache.CacheType)
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Cache<K, V> getCache(CacheType<K, V> type) {
		Map<Integer, Cache> cacheMap = getOrCreate();
		Cache<K, V> cache = cacheMap.get(type.getType());
		if (cacheFactory != null && cache == null) {
			cache = cacheFactory.create();
			addCache(type, cache);
		}
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.coordinator.CacheCoordinator#addCache(com.cetsoft
	 * .imcache.cache.CacheType, com.cetsoft.imcache.cache.Cache)
	 */
	public <K, V> void addCache(CacheType<K, V> type, Cache<K, V> cache) {
		Map<Integer, Cache> cacheMap = getOrCreate();
		cacheMap.put(type.getType(), cache);
	}

	protected Map<Integer, Cache> getOrCreate() {
		Map<Integer, Cache> cacheMap = cacheMapThreadLocal.get();
		if (cacheMap == null) {
			cacheMap = new HashMap<Integer, Cache>();
			cacheMapThreadLocal.set(new HashMap<Integer, Cache>());
		}
		return cacheMap;
	}
}
