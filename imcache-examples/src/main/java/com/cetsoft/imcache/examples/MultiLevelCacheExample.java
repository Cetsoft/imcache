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
* Date   : May 20, 2014
*/
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferStore;

/**
 * The Class MultiLevelCacheExample.
 */
public class MultiLevelCacheExample {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	@SuppressWarnings("null")
	public static void main(String [] args){
		final CacheDao cacheDao = null;// This is just for example purposes.
        OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(10000,10);
		final Cache<String,String> offHeapCache = CacheBuilder.offHeapCache().storage(bufferStore)
          .cacheLoader(new CacheLoader<String, String>() {
			public String load(String key) {
				return cacheDao.load(key);
			}
		}).evictionListener(new EvictionListener<String, String>() {
			public void onEviction(String key, String value) {
				cacheDao.store(key, value);
			}
		}).build();
		Cache<String,String> multiLevelCache = CacheBuilder.heapCache().cacheLoader(new CacheLoader<String, String>() {
			public String load(String key) {
				return offHeapCache.get(key);
			}
		}).evictionListener(new EvictionListener<String, String>() {
			public void onEviction(String key, String value) {
				offHeapCache.put(key, value);
			}
		}).capacity(10000).build();
		multiLevelCache.put("red","apple");
	}
	
	/**
	 * The Interface CacheDao.
	 */
	public static interface CacheDao {
		
		/**
		 * Load.
		 *
		 * @param key the key
		 * @return the string
		 */
		String load(String key);
		
		/**
		 * Store.
		 *
		 * @param key the key
		 * @param value the value
		 */
		void store(String key, String value);
	}
}
