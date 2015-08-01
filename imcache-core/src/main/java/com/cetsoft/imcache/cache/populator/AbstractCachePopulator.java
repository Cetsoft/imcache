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

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CachePopulator;

/**
 * The Class AbstractCachePopulator populates the cache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class AbstractCachePopulator<K, V> implements CachePopulator<K, V> {

	/** The cache. */
	protected Cache<K, V> cache;

	/**
	 * Instantiates a new abstract cache populator.
	 *
	 * @param cache the cache
	 */
	public AbstractCachePopulator(Cache<K, V> cache) {
		this.cache = cache;
	}
}
