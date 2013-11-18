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
public class LocalCacheCoordinator implements CacheCoordinator{
	
	CacheFactory cacheFactory;
	
	public LocalCacheCoordinator() {}

	public LocalCacheCoordinator(CacheFactory cacheFactory) {
		this.cacheFactory = cacheFactory;
	}

	/** The cache map thread local. */
	ThreadLocal<Map<Integer,Cache>> cacheMapThreadLocal = new ThreadLocal<Map<Integer,Cache>>(); 
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.coordinator.CacheCoordinator#getCache(com.cetsoft.imcache.cache.CacheType)
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Cache<K, V> getCache(CacheType<K, V> type) {
		Map<Integer,Cache> cacheMap = getOrCreate();
		Cache<K,V> cache = cacheMap.get(type.getType());
		if(cacheFactory!=null&&cache==null){
			cache = cacheFactory.create();
			addCache(type, cache);
		}
		return cache;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.coordinator.CacheCoordinator#addCache(com.cetsoft.imcache.cache.CacheType, com.cetsoft.imcache.cache.Cache)
	 */
	public <K, V> void addCache(CacheType<K, V> type, Cache<K, V> cache) {
		Map<Integer,Cache> cacheMap = getOrCreate();
		cacheMap.put(type.getType(), cache);
	}

	protected Map<Integer, Cache> getOrCreate() {
		Map<Integer, Cache> cacheMap = cacheMapThreadLocal.get();
		if(cacheMap==null){
			cacheMap = new HashMap<Integer, Cache>();
			cacheMapThreadLocal.set(new HashMap<Integer, Cache>());
		}
		return cacheMap;
	}
}
