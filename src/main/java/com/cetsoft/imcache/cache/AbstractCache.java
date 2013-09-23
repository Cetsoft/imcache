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
* Date   : Sep 16, 2013
*/
package com.cetsoft.imcache.cache;

/**
 * The Class AbstractCache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class AbstractCache<K, V> implements Cache<K, V> {

	/** The cache loader. */
	protected CacheLoader<K, V> cacheLoader;
	
	/** The eviction listener. */
	protected EvictionListener<K, V> evictionListener;
	
	public AbstractCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener) {
		this.cacheLoader = cacheLoader;
		this.evictionListener = evictionListener;
	}

	/**
	 * Gets the cache loader.
	 *
	 * @return the cache loader
	 */
	public CacheLoader<K, V> getCacheLoader() {
		return cacheLoader;
	}

	/**
	 * Sets the cache loader.
	 *
	 * @param cacheLoader the cache loader
	 */
	public void setCacheLoader(CacheLoader<K, V> cacheLoader) {
		this.cacheLoader = cacheLoader;
	}

	/**
	 * Gets the eviction listener.
	 *
	 * @return the eviction listener
	 */
	public EvictionListener<K, V> getEvictionListener() {
		return evictionListener;
	}

	/**
	 * Sets the eviction listener.
	 *
	 * @param evictionListener the eviction listener
	 */
	public void setEvictionListener(EvictionListener<K, V> evictionListener) {
		this.evictionListener = evictionListener;
	}

}
