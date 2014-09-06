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
 * Date   : Jan 6, 2014
 */
package com.cetsoft.imcache.examples;

import java.util.ArrayList;
import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.pupulator.ConcurrentCachePopulator;
import com.cetsoft.imcache.cache.util.CacheUtils;

/**
 * The Class CachePopulatorExample.
 */
public class CachePopulatorExample extends ConcurrentCachePopulator<String, String> {

	/**
	 * Instantiates a new cache populator example.
	 *
	 * @param cache the cache
	 */
	public CachePopulatorExample(Cache<String, String> cache) {
		super(cache);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.CachePopulator#loadEntries()
	 */
	public List<CacheEntry<String, String>> loadEntries() {
		final int SIZE = 3;
		List<CacheEntry<String, String>> cacheEntries = new ArrayList<CacheEntry<String, String>>(SIZE);
		for (int i = 0; i < SIZE; i++) {
			cacheEntries.add(CacheUtils.createEntry("" + i, "" + i));
		}
		return cacheEntries;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Cache<String, String> cache = CacheBuilder.concurrentHeapCache().build();
		CachePopulatorExample populatorExample = new CachePopulatorExample(cache);
		populatorExample.pupulate();
		System.out.println(cache.get("0"));
	}

}
