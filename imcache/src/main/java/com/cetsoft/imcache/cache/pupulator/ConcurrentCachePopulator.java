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
* Date   : Jan 4, 2014
*/
package com.cetsoft.imcache.cache.pupulator;

import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;

/**
 * The Class ConcurrentCachePopulator initializes threads to populate the cache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class ConcurrentCachePopulator<K, V> extends AbstractCachePopulator<K, V>{

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

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.CachePopulator#pupulate()
	 */
	public void pupulate() {
		final List<CacheEntry<K, V>> entries = loadEntries();
		if(entries.size()<concurrencyLevel){
			concurrencyLevel = entries.size();
		}
		final int partition = entries.size()/concurrencyLevel;
		for (int i = 0; i < concurrencyLevel; i++) {
			final int start = i*partition;
			final int stop = i!=concurrencyLevel-1?(i+1)*partition:entries.size();
			new Thread(new Runnable() {
				public void run() {
					for (int j = start; j < stop; j++) {
						cache.put(entries.get(j).getKey(), entries.get(j).getValue());
					}
				}
			}, "imcache:cachePopulator(name="+cache.getName()+",thread="+i+")").start();
		}
	}

}
