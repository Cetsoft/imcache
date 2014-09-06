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
package com.cetsoft.imcache.cache.builder;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.offheap.OffHeapCache;
import com.cetsoft.imcache.cache.offheap.VersionedOffHeapCache;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class VersionedOffHeapCacheBuilder.
 */
public class VersionedOffHeapCacheBuilder extends CacheBuilder {

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
	public VersionedOffHeapCacheBuilder() {
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
	public VersionedOffHeapCacheBuilder storage(OffHeapByteBufferStore bufferStore) {
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
	public <V> VersionedOffHeapCacheBuilder serializer(Serializer<V> serializer) {
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
	public <K, V> VersionedOffHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader) {
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
	public <K, V> VersionedOffHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener) {
		this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
		return this;
	}

	/**
	 * Query executer.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param indexHandler the query executer
	 * @return the versioned off heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> VersionedOffHeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler) {
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
	public VersionedOffHeapCacheBuilder concurrencyLevel(int concurrencyLevel) {
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
	public VersionedOffHeapCacheBuilder evictionPeriod(long evictionPeriod) {
		this.evictionPeriod = evictionPeriod;
		return this;
	}

	/**
	 * Buffer cleaner period.
	 *
	 * @param bufferCleanerPeriod the buffer cleaner period
	 * @return the off heap cache builder
	 */
	public VersionedOffHeapCacheBuilder bufferCleanerPeriod(long bufferCleanerPeriod) {
		this.bufferCleanerPeriod = bufferCleanerPeriod;
		return this;
	}

	/**
	 * Buffer cleaner threshold.
	 *
	 * @param bufferCleanerThreshold the buffer cleaner threshold
	 * @return the off heap cache builder
	 */
	public VersionedOffHeapCacheBuilder bufferCleanerThreshold(float bufferCleanerThreshold) {
		this.bufferCleanerThreshold = bufferCleanerThreshold;
		return this;
	}

	/**
	 * Adds the index.
	 *
	 * @param attributeName the attribute name
	 * @param indexType the index type
	 * @return the versioned off heap cache builder
	 */
	public VersionedOffHeapCacheBuilder addIndex(String attributeName, IndexType indexType) {
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
		return (SearchableCache<K, V>) new VersionedOffHeapCache<K, V>(byteBufferStore, (Serializer<V>) serializer,
				(CacheLoader<K, V>) cacheLoader, (EvictionListener<K, V>) evictionListener,
				(IndexHandler<K, V>) indexHandler, bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel,
				evictionPeriod);
	}

}
