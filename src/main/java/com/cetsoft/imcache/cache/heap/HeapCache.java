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
* Date   : Sep 16, 2013
*/
package com.cetsoft.imcache.cache.heap;

import java.util.LinkedHashMap;
import java.util.Map;

import com.cetsoft.imcache.cache.AbstractCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;

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
public class HeapCache<K,V> extends AbstractCache<K, V> {
	
	/** The hit. */
	protected long hit;
	
	/** The miss. */
	protected long miss;
	
	/** The cache. */
	protected Map<K,V> cache;
	
	/** The default capacity. */
	private final int DEFAULT_CAPACITY = 1009;
	
	/**
	 * Instantiates a new heap cache.
	 */
	public HeapCache() {
		super();
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new heap cache.
	 *
	 * @param cacheLoader the cache loader
	 */
	public HeapCache(CacheLoader<K, V> cacheLoader) {
		super(cacheLoader);
		initCache(DEFAULT_CAPACITY);
	}
	

	/**
	 * Instantiates a new heap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param capacity the capacity
	 */
	public HeapCache(CacheLoader<K, V> cacheLoader,int capacity) {
		super(cacheLoader);
		initCache(capacity);
	}
	
	/**
	 * Instantiates a new heap cache.
	 *
	 * @param evictionListener the eviction listener
	 */
	public HeapCache(EvictionListener<K, V> evictionListener) {
		super(evictionListener);
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new heap cache.
	 *
	 * @param evictionListener the eviction listener
	 * @param capacity the capacity
	 */
	public HeapCache(EvictionListener<K, V> evictionListener, int capacity) {
		super(evictionListener);
		initCache(capacity);
	}
	
	/**
	 * Instantiates a new heap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 */
	public HeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener) {
		super(cacheLoader,evictionListener);
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new heap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param capacity the capacity
	 */
	public HeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener, int capacity) {
		super(cacheLoader,evictionListener);
		initCache(capacity);
	}
	
	/**
	 * Inits the cache.
	 *
	 * @param capacity the capacity
	 */
	private void initCache(int capacity){
		cache = new LimitedHashMap(capacity);
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	public void put(K key, V value) {
		cache.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#get(java.lang.Object)
	 */
	public V get(K key) {
		return cache.get(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#invalidate(java.lang.Object)
	 */
	public V invalidate(K key) {
		return cache.remove(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(K key) {
		return cache.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#clear()
	 */
	public void clear() {
		for (K key : cache.keySet()) {
			cache.remove(key);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#hitRatio()
	 */
	public double hitRatio() {
		return hit/(hit+miss);
	}
	
	/**
	 * The Class LimitedHashMap.
	 */
	private class LimitedHashMap extends LinkedHashMap<K,V>{

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
		
		/* (non-Javadoc)
		 * @see java.util.LinkedHashMap#get(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public V get(Object key){
			V value = super.get(key);
			if(value==null){
				miss++;
				value = HeapCache.this.cacheLoader.load((K)key);
			}
			else{
				hit++;
			}
			return value;
		}
		
		/* (non-Javadoc)
		 * @see java.util.HashMap#remove(java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public V remove(Object key){
			V value = super.remove(key);
			if(value!=null){
				HeapCache.this.evictionListener.onEviction((K) key, value);
			}
			return value;
		}
		
		/* (non-Javadoc)
		 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
		 */
		@Override
		protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
			boolean shouldRemove = size() > this.capacity;
			HeapCache.this.evictionListener.onEviction(eldest.getKey(), eldest.getValue());
			return shouldRemove;
		}
		
	}

}
