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
