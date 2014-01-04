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
package com.cetsoft.imcache.cache.util;

import com.cetsoft.imcache.cache.CacheEntry;

/**
 * The Class CacheUtils.
 */
public class CacheUtils {

	/**
	 * Creates the entry.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param key the key
	 * @param value the value
	 * @return the cache entry
	 */
	public static <K, V> CacheEntry<K, V> createEntry(K key, V value){
		return new DefaultCacheEntry<K, V>(key, value);
	}
	
	/**
	 * The Class DefaultCacheEntry.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	protected static class DefaultCacheEntry<K,V> implements CacheEntry<K, V>{

		/** The key. */
		private K key;
		
		/** The value. */
		private V value;
		
		/**
		 * Instantiates a new default cache entry.
		 *
		 * @param key the key
		 * @param value the value
		 */
		public DefaultCacheEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.CacheEntry#getKey()
		 */
		public K getKey() {
			return key;
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.CacheEntry#getValue()
		 */
		public V getValue() {
			return value;
		}
		
	}
}
