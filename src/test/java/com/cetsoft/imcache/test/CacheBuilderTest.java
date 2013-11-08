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
package com.cetsoft.imcache.test;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.search.CacheQuery;
import com.cetsoft.imcache.cache.search.criteria.ETCriteria;
import com.cetsoft.imcache.cache.search.index.IndexType;

/**
 * The Class CacheBuilderTest.
 */
public class CacheBuilderTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main (String [] args){
		Cache<Integer,Integer> cache = CacheBuilder.heapCache().cacheLoader(new CacheLoader<Integer, Integer>() {
			public Integer load(Integer key) {
				return null;
			}
		}).capacity(10000).build();
		cache.get(0);
		SearchableCache<Integer, VersionedItem<Integer>> searchableCache = CacheBuilder.
				versionedOffHeapCache().addIndex("ada", IndexType.UNIQUE_HASH).build();
		searchableCache.execute(CacheQuery.newQuery().setCriteria(new ETCriteria("ada", 24)));
	}
}
