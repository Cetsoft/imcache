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
* Date   : Sep 17, 2013
*/
package com.cetsoft.imcache.heap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.AbstractCache;
import com.cetsoft.imcache.CacheLoader;
import com.cetsoft.imcache.EvictionListener;
import com.cetsoft.imcache.concurrent.ConcurrentLinkedHashMap;

/**
 * The Class ConcurrentHeapCache.
 * ConcurrentHeapCache uses LRU(Least Recently Used) as eviction 
 * strategy by the help of ConcurrentLinkedHashMap. As a result, 
 * ConcurrentHeapCache discards the least recently used items first
 * when eviction required. Eviction occurs if the size of
 * the cache is equal to the cache capacity in a put operation
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ConcurrentHeapCache<K, V> extends AbstractCache<K, V> {

	/** The hit. */
	protected AtomicLong hit = new AtomicLong();

	/** The miss. */
	protected AtomicLong miss = new AtomicLong();

	/** The cache. */
	protected Map<K,V> cache;
	
	/** The default capacity. */
	private final int DEFAULT_CAPACITY = 1009;
	
	/**
	 * Instantiates a new heap cache.
	 */
	public ConcurrentHeapCache() {
		super();
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new concurrent heap cache.
	 *
	 * @param cacheLoader the cache loader
	 */
	public ConcurrentHeapCache(CacheLoader<K, V> cacheLoader) {
		super(cacheLoader);
		initCache(DEFAULT_CAPACITY);
	}
	

	/**
	 * Instantiates a new concurrent heap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param capacity the capacity
	 */
	public ConcurrentHeapCache(CacheLoader<K, V> cacheLoader,int capacity) {
		super(cacheLoader);
		initCache(capacity);
	}
	
	/**
	 * Instantiates a new concurrent heap cache.
	 *
	 * @param evictionListener the eviction listener
	 */
	public ConcurrentHeapCache(EvictionListener<K, V> evictionListener) {
		super(evictionListener);
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new concurrent heap cache.
	 *
	 * @param evictionListener the eviction listener
	 * @param capacity the capacity
	 */
	public ConcurrentHeapCache(EvictionListener<K, V> evictionListener, int capacity) {
		super(evictionListener);
		initCache(capacity);
	}
	
	/**
	 * Instantiates a new concurrent heap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 */
	public ConcurrentHeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener) {
		super(cacheLoader,evictionListener);
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new concurrent heap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param capacity the capacity
	 */
	public ConcurrentHeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener, int capacity) {
		super(cacheLoader,evictionListener);
		initCache(capacity);
	}
	
	/**
	 * Inits the cache.
	 *
	 * @param capacity the capacity
	 */
	private void initCache(int capacity){
		cache = new ConcurrentLimitedHashMap(capacity);
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	public void put(K key, V value) {
		cache.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#get(java.lang.Object)
	 */
	public V get(K key) {
		return cache.get(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#invalidate(java.lang.Object)
	 */
	public V invalidate(K key) {
		return cache.remove(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(K key) {
		return cache.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#clear()
	 */
	public void clear() {
		for (K key : cache.keySet()) {
			cache.remove(key);
		}
	}
	
	public double hitRatio() {
		return hit.get() / (hit.get() + miss.get());
	}
	
	private class ConcurrentLimitedHashMap extends ConcurrentLinkedHashMap<K, V> implements Map<K,V>{
		
		private static final long serialVersionUID = -1816555501039461556L;
		
		private int capacity;
		
		public ConcurrentLimitedHashMap(int capacity) {
			this.capacity = capacity;
		}
		
		@Override
		public V put(K key, V value){
			if(capacity==this.size()){
				Entry<K,V> entry = this.removeEldestEntry();
				ConcurrentHeapCache.this.evictionListener.onEviction(entry.getKey(), entry.getValue());
			}
			V exValue = super.put(key, value);
			return exValue;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public V get(Object key){
			V value = super.get(key);
			if(value==null){
				miss.incrementAndGet();
				value = ConcurrentHeapCache.this.cacheLoader.load((K)key);
			}
			else{
				hit.incrementAndGet();
			}
			return value;
		}

		@Override
		@SuppressWarnings("unchecked")
		public V remove(Object key){
			V value = super.remove(key);
			if(value!=null){
				ConcurrentHeapCache.this.evictionListener.onEviction((K) key, value);
			}
			return value;
		}
		
	}
	
}