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
* Date   : Sep 20, 2013
*/
package com.cetsoft.imcache.offheap;

import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.AbstractCache;
import com.cetsoft.imcache.CacheLoader;
import com.cetsoft.imcache.EvictionListener;

/**
 * The Class OffHeapCache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class OffHeapCache<K,V> extends AbstractCache<K, V> {
	
	/** The Constant DEFAULT_CAPACITY. */
	private static final int DEFAULT_CAPACITY =  100; 
	
	/** The hit. */
	protected AtomicLong hit = new AtomicLong();

	/** The miss. */
	protected AtomicLong miss = new AtomicLong();

	/**
	 * Instantiates a new heap cache.
	 */
	public OffHeapCache() {
		super();
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new offheap cache.
	 *
	 * @param cacheLoader the cache loader
	 */
	public OffHeapCache(CacheLoader<K, V> cacheLoader) {
		super(cacheLoader);
		initCache(DEFAULT_CAPACITY);
	}
	

	/**
	 * Instantiates a new offheap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param capacity the capacity
	 */
	public OffHeapCache(CacheLoader<K, V> cacheLoader,int capacity) {
		super(cacheLoader);
		initCache(capacity);
	}
	
	/**
	 * Instantiates a new offheap cache.
	 *
	 * @param evictionListener the eviction listener
	 */
	public OffHeapCache(EvictionListener<K, V> evictionListener) {
		super(evictionListener);
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new offheap cache.
	 *
	 * @param evictionListener the eviction listener
	 * @param capacity the capacity
	 */
	public OffHeapCache(EvictionListener<K, V> evictionListener, int capacity) {
		super(evictionListener);
		initCache(capacity);
	}
	
	/**
	 * Instantiates a new offheap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 */
	public OffHeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener) {
		super(cacheLoader,evictionListener);
		initCache(DEFAULT_CAPACITY);
	}
	
	/**
	 * Instantiates a new offheap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param capacity the capacity
	 */
	public OffHeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener, int capacity) {
		super(cacheLoader,evictionListener);
		initCache(capacity);
	}
	
	/**
	 * Inits the cache.
	 *
	 * @param capacity the capacity
	 */
	private void initCache(int capacity){
		
	}

	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	public void put(K key, V value) {
		
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#get(java.lang.Object)
	 */
	public V get(K key) {

		return null;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#invalidate(java.lang.Object)
	 */
	public V invalidate(K key) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(K key) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#clear()
	 */
	public void clear() {
		
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.Cache#hitRatio()
	 */
	public double hitRatio() {
		return 0;
	}

}
