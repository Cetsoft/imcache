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
 * Date   : Jan 4, 2014
 */
package com.cetsoft.imcache.cache.pupulator;

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
