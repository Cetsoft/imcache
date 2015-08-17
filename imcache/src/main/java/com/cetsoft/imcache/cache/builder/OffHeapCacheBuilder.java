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
package com.cetsoft.imcache.cache.builder;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.offheap.OffHeapCache;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class OffHeapCacheBuilder.
 */
public class OffHeapCacheBuilder extends SearchableCacheBuilder {

	/** The concurrency level. */
	int concurrencyLevel;

	/** The eviction period. */
	long evictionPeriod;

	/** The buffer cleaner period. */
	long bufferCleanerPeriod;

	/** The serializer. */
	Serializer<Object> serializer;

	/** The buffer cleaner threshold. */
	float bufferCleanerThreshold;

	/** The byte buffer store. */
	OffHeapByteBufferStore byteBufferStore;

	/**
	 * Instantiates a new off heap cache builder.
	 */
	public OffHeapCacheBuilder() {
		super();
		concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;
		evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;
		bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;
		bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;
		serializer = SERIALIZER;
	}

	/**
	 * Storage.
	 *
	 * @param bufferStore the buffer store
	 * @return the off heap cache builder
	 */
	public OffHeapCacheBuilder storage(OffHeapByteBufferStore bufferStore) {
		this.byteBufferStore = bufferStore;
		return this;
	}

	/**
	 * Serializer.
	 *
	 * @param <V> the value type
	 * @param serializer the serializer
	 * @return the off heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <V> OffHeapCacheBuilder serializer(Serializer<V> serializer) {
		this.serializer = (Serializer<Object>) serializer;
		return this;
	}

	/**
	 * Cache loader.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param cacheLoader the cache loader
	 * @return the off heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> OffHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader) {
		this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
		return this;
	}

	/**
	 * Eviction listener.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param evictionListener the eviction listener
	 * @return the off heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> OffHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener) {
		this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
		return this;
	}

	/**
	 * Query executer.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param indexHandler the query executer
	 * @return the off heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> OffHeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler) {
		this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
		isSearchable = true;
		return this;
	}

	/**
	 * Concurrency level.
	 *
	 * @param concurrencyLevel the concurrency level
	 * @return the off heap cache builder
	 */
	public OffHeapCacheBuilder concurrencyLevel(int concurrencyLevel) {
		if (concurrencyLevel > 11 && concurrencyLevel < 0) {
			throw new IllegalArgumentException("ConcurrencyLevel must be between 0 and 11 inclusive.");
		}
		this.concurrencyLevel = concurrencyLevel;
		return this;
	}

	/**
	 * Eviction period.
	 *
	 * @param evictionPeriod the eviction period
	 * @return the off heap cache builder
	 */
	public OffHeapCacheBuilder evictionPeriod(long evictionPeriod) {
		this.evictionPeriod = evictionPeriod;
		return this;
	}

	/**
	 * Buffer cleaner period.
	 *
	 * @param bufferCleanerPeriod the buffer cleaner period
	 * @return the off heap cache builder
	 */
	public OffHeapCacheBuilder bufferCleanerPeriod(long bufferCleanerPeriod) {
		this.bufferCleanerPeriod = bufferCleanerPeriod;
		return this;
	}

	/**
	 * Buffer cleaner threshold.
	 *
	 * @param bufferCleanerThreshold the buffer cleaner threshold
	 * @return the off heap cache builder
	 */
	public OffHeapCacheBuilder bufferCleanerThreshold(float bufferCleanerThreshold) {
		this.bufferCleanerThreshold = bufferCleanerThreshold;
		return this;
	}

	/**
	 * Adds the index.
	 *
	 * @param attributeName the attribute name
	 * @param indexType the index type
	 * @return the off heap cache builder
	 */
	public OffHeapCacheBuilder addIndex(String attributeName, IndexType indexType) {
		searchable();
		indexHandler.addIndex(attributeName, indexType);
		return this;
	}

	/**
	 * Builds the cache.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return the cache
	 */
	@SuppressWarnings("unchecked")
	public <K, V> SearchableCache<K, V> build() {
		if (this.byteBufferStore == null) {
			throw new NecessaryArgumentException("ByteBufferStore must be set!");
		}
		return new OffHeapCache<K, V>((CacheLoader<K, V>) cacheLoader, (EvictionListener<K, V>) evictionListener,
				(IndexHandler<K, V>) indexHandler, byteBufferStore, (Serializer<V>) serializer, bufferCleanerPeriod,
				bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
	}
	
	/**
	 * Builds the cache.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param cacheName the cache name
	 * @return the searchable cache
	 */
	public <K, V> SearchableCache<K, V> build(String cacheName) {
		SearchableCache<K, V> cache = build();
		cache.setName(cacheName);
		return cache;
	}
}