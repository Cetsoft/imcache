/*
* Copyright (C) 2013 Yusuf Aytas, http://www.yusufaytas.com
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
* Date   : Dec 29, 2013
*/
package com.cetsoft.imcache.spring;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.cetsoft.imcache.cache.builder.CacheBuilder;

/**
 * The Class ImcacheCacheManager.
 */
public class ImcacheCacheManager implements CacheManager, InitializingBean {

	/** The cache builder. */
	private CacheBuilder cacheBuilder;
	
	/** The caches. */
	private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();

	/**
	 * Instantiates a new imcache cache manager.
	 */
	public ImcacheCacheManager() {
		cacheBuilder = CacheBuilder.concurrentHeapCache();
	}

	/**
	 * Instantiates a new imcache cache manager.
	 *
	 * @param cacheBuilder the cache builder
	 */
	public ImcacheCacheManager(CacheBuilder cacheBuilder) {
		this.cacheBuilder = cacheBuilder;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.CacheManager#getCache(java.lang.String)
	 */
	public Cache getCache(String name) {
		Cache cache = caches.get(name);
		if (cache == null) {
			ImcacheCache newCache = new ImcacheCache(cacheBuilder.build(name));
			final Cache exCache = caches.putIfAbsent(name, newCache);
			if (exCache != null) {
				cache = exCache;
			}
			else{
				cache = newCache;
			}
		}
		return cache;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.CacheManager#getCacheNames()
	 */
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableCollection(caches.keySet());
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		
	}

	/**
	 * Gets the cache builder.
	 *
	 * @return the cache builder
	 */
	public CacheBuilder getCacheBuilder() {
		return cacheBuilder;
	}

	/**
	 * Sets the cache builder.
	 *
	 * @param cacheBuilder the new cache builder
	 */
	public void setCacheBuilder(CacheBuilder cacheBuilder) {
		this.cacheBuilder = cacheBuilder;
	}

}
