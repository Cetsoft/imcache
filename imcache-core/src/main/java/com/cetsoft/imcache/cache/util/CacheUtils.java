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
	public static <K, V> CacheEntry<K, V> createEntry(K key, V value) {
		return new DefaultCacheEntry<K, V>(key, value);
	}

	/**
	 * The Class DefaultCacheEntry.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	protected static class DefaultCacheEntry<K, V> implements CacheEntry<K, V> {

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

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.CacheEntry#getKey()
		 */
		public K getKey() {
			return key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.CacheEntry#getValue()
		 */
		public V getValue() {
			return value;
		}

	}
}
