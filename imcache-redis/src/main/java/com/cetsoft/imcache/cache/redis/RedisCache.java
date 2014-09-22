/*
* Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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
* Date   : Sep 4, 2014
*/
package com.cetsoft.imcache.cache.redis;

import com.cetsoft.imcache.cache.AbstractCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.redis.client.RedisClient;
import com.cetsoft.imcache.cache.search.IndexHandler;

public class RedisCache<K, V> extends AbstractCache<K, V> {

    protected RedisClient client = null;

	public RedisCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener,
			IndexHandler<K, V> indexHandler) {
		super(cacheLoader, evictionListener, indexHandler);
	}

	public void put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	public V get(K key) {
		throw new UnsupportedOperationException();
	}

	public V invalidate(K key) {
		throw new UnsupportedOperationException();
	}

	public boolean contains(K key) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public double hitRatio() {
		throw new UnsupportedOperationException();
	}
}
