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
 * The Class LazyCachePupulator populates the cache slowly after it is called.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class LazyCachePupulator<K, V> extends AbstractCachePopulator<K, V> {

	/**
	 * Instantiates a new lazy cache pupulator.
	 *
	 * @param cache the cache
	 */
	public LazyCachePupulator(Cache<K, V> cache) {
		super(cache);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.CachePopulator#pupulate()
	 */
	public void pupulate() {
		new Thread(new Runnable() {
			public void run() {
				List<CacheEntry<K, V>> entries = loadEntries();
				for (CacheEntry<K, V> cacheEntry : entries) {
					cache.put(cacheEntry.getKey(), cacheEntry.getValue());
				}
			}
		}, "imcache:cachePopulator(name=" + cache.getName() + ",thread=0)").start();
	}

}
