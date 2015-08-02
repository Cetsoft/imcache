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
* Date   : Jan 4, 2014
*/
package com.cetsoft.imcache.cache.populator;

import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;

/**
 * The Class ConcurrentCachePopulator initializes threads to populate the cache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class ConcurrentCachePopulator<K, V> extends AbstractCachePopulator<K, V> {

	/** The concurrency level. */
	private int concurrencyLevel;

	/** The Constant DEFAULT_CONCURRENCY_LEVEL. */
	private final static int DEFAULT_CONCURRENCY_LEVEL = 11;

	/**
	 * Instantiates a new concurrent cache populator.
	 *
	 * @param cache the cache
	 * @param concurrencyLevel the concurrency level
	 */
	public ConcurrentCachePopulator(Cache<K, V> cache, int concurrencyLevel) {
		super(cache);
		this.concurrencyLevel = concurrencyLevel;
	}

	/**
	 * Instantiates a new concurrent cache populator.
	 *
	 * @param cache the cache
	 */
	public ConcurrentCachePopulator(Cache<K, V> cache) {
		this(cache, DEFAULT_CONCURRENCY_LEVEL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.CachePopulator#pupulate()
	 */
	public void pupulate() {
		final List<CacheEntry<K, V>> entries = loadEntries();
		if (entries.size() < concurrencyLevel) {
			concurrencyLevel = entries.size();
		}
		final int partition = entries.size() / concurrencyLevel;
		for (int i = 0; i < concurrencyLevel; i++) {
			final int start = i * partition;
			final int stop = i != concurrencyLevel - 1 ? (i + 1) * partition : entries.size();
			new Thread(new Runnable() {
				public void run() {
					for (int j = start; j < stop; j++) {
						cache.put(entries.get(j).getKey(), entries.get(j).getValue());
					}
				}
			}, "imcache:cachePopulator(name=" + cache.getName() + ",thread=" + i + ")").start();
		}
	}

}
