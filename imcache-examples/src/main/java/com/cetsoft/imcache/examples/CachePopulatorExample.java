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
* Date   : Jan 6, 2014
*/
package com.cetsoft.imcache.examples;

import java.util.ArrayList;
import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.populator.ConcurrentCachePopulator;
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
	
	public static void example() {
		Cache<String, String> cache = CacheBuilder.concurrentHeapCache().build();
		CachePopulatorExample populatorExample = new CachePopulatorExample(cache);
		populatorExample.pupulate();
		System.out.println(cache.get("0"));
	}

	public static void main(String[] args) {
		example();
	}

}
