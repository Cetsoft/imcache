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
package com.cetsoft.imcache.spring;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.builder.ConcurrentHeapCacheBuilder;
import com.cetsoft.imcache.cache.builder.HeapCacheBuilder;
import com.cetsoft.imcache.cache.builder.OffHeapCacheBuilder;
import com.cetsoft.imcache.cache.builder.TransactionalHeapCacheBuilder;
import com.cetsoft.imcache.cache.builder.VersionedOffHeapCacheBuilder;
import com.cetsoft.imcache.cache.offheap.OffHeapCache;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class SpringCacheBuilder.
 */
public class SpringCacheBuilder extends CacheBuilder {

	/** The type. */
	private String type;
	
	/** The concurrency level. */
	private int concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;
	
	/** The eviction period. */
	private long evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;
	
	/** The buffer cleaner period. */
	private long bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;
	
	/** The buffer cleaner threshold. */
	private float bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;
	
	/** The serializer. */
	private Serializer<Object> serializer = SERIALIZER;
	
	/** The buffer store. */
	private OffHeapByteBufferStore bufferStore;
	
	/**
	 * Instantiates a new spring cache builder.
	 */
	public SpringCacheBuilder() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.builder.CacheBuilder#build()
	 */
	@Override
	public <K, V> SearchableCache<K, V> build() {
		SearchableCache<K, V> cache = null;
		if (type==null||type.equals("concurrentheap")) {
			ConcurrentHeapCacheBuilder builder = CacheBuilder.concurrentHeapCache();
			builder.cacheLoader(cacheLoader).evictionListener(evictionListener).indexHandler(indexHandler);
			cache = builder.build();
		} else if (type.equals("heap")) {
			HeapCacheBuilder builder = CacheBuilder.heapCache();
			builder.cacheLoader(cacheLoader).evictionListener(evictionListener).indexHandler(indexHandler);
			cache = builder.build();
		} else if (type.equals("transactionalheap")) {
			TransactionalHeapCacheBuilder builder = CacheBuilder.transactionalHeapCache();
			builder.cacheLoader(cacheLoader).evictionListener(evictionListener).indexHandler(indexHandler);
			cache = builder.build();
		} else if (type.equals("offheap")) {
			OffHeapCacheBuilder builder = CacheBuilder.offHeapCache();
			builder.cacheLoader(cacheLoader).evictionListener(evictionListener).indexHandler(indexHandler).
			concurrencyLevel(concurrencyLevel).bufferCleanerPeriod(bufferCleanerPeriod).bufferCleanerThreshold(bufferCleanerThreshold).
			evictionPeriod(evictionPeriod).serializer(serializer).storage(bufferStore);
			cache = builder.build();
		} else if (type.equals("versionedoffheap")) {
			VersionedOffHeapCacheBuilder builder = CacheBuilder.versionedOffHeapCache();
			builder.cacheLoader(cacheLoader).evictionListener(evictionListener).indexHandler(indexHandler).
			concurrencyLevel(concurrencyLevel).bufferCleanerPeriod(bufferCleanerPeriod).bufferCleanerThreshold(bufferCleanerThreshold).
			evictionPeriod(evictionPeriod).serializer(serializer).storage(bufferStore);
			cache = builder.build();
		}
		return cache;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Sets the concurrency level.
	 *
	 * @param concurrencyLevel the new concurrency level
	 */
	public void setConcurrencyLevel(int concurrencyLevel) {
		this.concurrencyLevel = concurrencyLevel;
	}

	/**
	 * Sets the eviction period.
	 *
	 * @param evictionPeriod the new eviction period
	 */
	public void setEvictionPeriod(long evictionPeriod) {
		this.evictionPeriod = evictionPeriod;
	}

	/**
	 * Sets the buffer cleaner period.
	 *
	 * @param bufferCleanerPeriod the new buffer cleaner period
	 */
	public void setBufferCleanerPeriod(long bufferCleanerPeriod) {
		this.bufferCleanerPeriod = bufferCleanerPeriod;
	}

	/**
	 * Sets the buffer cleaner threshold.
	 *
	 * @param bufferCleanerThreshold the new buffer cleaner threshold
	 */
	public void setBufferCleanerThreshold(float bufferCleanerThreshold) {
		this.bufferCleanerThreshold = bufferCleanerThreshold;
	}

	/**
	 * Sets the serializer.
	 *
	 * @param serializer the new serializer
	 */
	public void setSerializer(Serializer<Object> serializer) {
		this.serializer = serializer;
	}

	/**
	 * Sets the cache loader.
	 *
	 * @param cacheLoader the cache loader
	 */
	public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
		this.cacheLoader = cacheLoader;
	}

	/**
	 * Sets the eviction listener.
	 *
	 * @param evictionListener the eviction listener
	 */
	public void setEvictionListener(EvictionListener<Object, Object> evictionListener) {
		this.evictionListener = evictionListener;
	}

	/**
	 * Sets the index handler.
	 *
	 * @param indexHandler the index handler
	 */
	public void setIndexHandler(IndexHandler<Object, Object> indexHandler) {
		this.indexHandler = indexHandler;
	}

	/**
	 * Sets the buffer store.
	 *
	 * @param bufferStore the new buffer store
	 */
	public void setBufferStore(OffHeapByteBufferStore bufferStore) {
		this.bufferStore = bufferStore;
	}

}
