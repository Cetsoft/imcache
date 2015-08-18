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
* Date   : Sep 16, 2013
*/
package com.cetsoft.imcache.heap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cetsoft.imcache.cache.AbstractSearchableCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;

/**
 * The Class HeapCache.
 * HeapCache uses LRU(Least Recently Used) as eviction 
 * strategy by the help of LinkedHashMap. As a result, 
 * HeapCache discards the least recently used items first
 * when eviction required. Eviction occurs if the size of
 * the cache is equal to the cache capacity in a put 
 * operation
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class HeapCache<K, V> extends AbstractSearchableCache<K, V> {

	/** The hit. */
	protected long hit;

	/** The miss. */
	protected long miss;

	/** The cache. */
	protected Map<K, V> cache;

	/**
	 * Instantiates a new heap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the query executer
	 * @param capacity the capacity
	 */
	public HeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener,
			IndexHandler<K, V> indexHandler, int capacity) {
		super(cacheLoader, evictionListener, indexHandler);
		initCache(capacity);
	}

	/**
	 * Inits the cache.
	 *
	 * @param capacity the capacity
	 */
	private void initCache(int capacity) {
		cache = new LimitedHashMap(capacity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public void put(K key, V value) {
		cache.put(key, value);
		indexHandler.add(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#get(java.lang.Object)
	 */
	public V get(K key) {
		return cache.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#invalidate(java.lang.Object)
	 */
	public V invalidate(K key) {
		V value = cache.remove(key);
		indexHandler.remove(key, value);
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(K key) {
		return cache.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#clear()
	 */
	public void clear() {
        List<Object> list = new ArrayList<Object>(cache.keySet());
        for (Object object : list) {
        	cache.remove(object);
		}
		indexHandler.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#hitRatio()
	 */
	public double hitRatio() {
		return hitRatio(hit, miss);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#size()
	 */
        public int size() {
                return cache.size();
        }
        
	/**
	 * The Class LimitedHashMap.
	 */
	protected class LimitedHashMap extends LinkedHashMap<K, V> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -831411504252696399L;

		/** The capacity. */
		private int capacity;

		/**
		 * Instantiates a new limited hash map.
		 *
		 * @param capacity the capacity
		 */
		public LimitedHashMap(int capacity) {
			super(capacity, 0.75f, true);
			this.capacity = capacity;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.LinkedHashMap#get(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public V get(Object key) {
			V value = super.get(key);
			if (value == null) {
				miss++;
				value = HeapCache.this.cacheLoader.load((K) key);
				if (value != null) {
					cache.put((K) key, value);
				}
			} else {
				hit++;
			}
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.HashMap#remove(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public V remove(Object key) {
			V value = super.remove(key);
			if (value != null) {
				HeapCache.this.evictionListener.onEviction((K) key, value);
			}
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
		 */
		@Override
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			boolean shouldRemove = size() > this.capacity;
			if (shouldRemove) {
				HeapCache.this.evictionListener.onEviction(eldest.getKey(), eldest.getValue());
			}
			return shouldRemove;
		}

	}
        
}
