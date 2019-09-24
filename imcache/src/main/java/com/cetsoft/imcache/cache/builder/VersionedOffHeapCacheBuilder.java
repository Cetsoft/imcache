/**
 * Copyright © 2013 Cetsoft. All rights reserved.
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
 */
package com.cetsoft.imcache.cache.builder;

import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotEmpty;
import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotNull;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.offheap.OffHeapCache;
import com.cetsoft.imcache.offheap.VersionedOffHeapCache;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.serialization.Serializer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class VersionedOffHeapCacheBuilder.
 */
public class VersionedOffHeapCacheBuilder extends BaseCacheBuilder {

  /**
   * Cache number
   */
  private final AtomicInteger cacheNumber = new AtomicInteger();
  /**
   * The concurrency level.
   */
  private int concurrencyLevel;
  /**
   * The eviction period.
   */
  private long evictionPeriod;
  /**
   * The buffer cleaner period.
   */
  private long bufferCleanerPeriod;
  /**
   * The serializer.
   */
  private Serializer<Object> serializer;
  /**
   * The buffer cleaner threshold.
   */
  private float bufferCleanerThreshold;
  /**
   * The byte buffer store.
   */
  private OffHeapByteBufferStore byteBufferStore;

  /**
   * Instantiates a new off heap cache builder.
   */
  public VersionedOffHeapCacheBuilder() {
    super();
    name = "imcache-versioned-offheap-cache-" + cacheNumber.incrementAndGet();
    serializer = DEFAULT_SERIALIZER;
    concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;
    evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;
    bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;
    bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;
  }

  /**
   * Name redis version off heap cache.
   *
   * @param name the name
   * @return the versioned offheap cache builder
   */
  public VersionedOffHeapCacheBuilder name(final String name) {
    checkNotEmpty(name, "name can't be null or empty");
    this.name = name;
    return this;
  }

  /**
   * Storage.
   *
   * @param bufferStore the buffer store
   * @return the off heap cache builder
   */
  public VersionedOffHeapCacheBuilder storage(final OffHeapByteBufferStore bufferStore) {
    checkNotNull(bufferStore, "buffer store can't be null");
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
  public <V> VersionedOffHeapCacheBuilder serializer(final Serializer<V> serializer) {
    checkNotNull(serializer, "serializer store can't be null");
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
  public <K, V> VersionedOffHeapCacheBuilder cacheLoader(final CacheLoader<K, V> cacheLoader) {
    checkNotNull(cacheLoader, "cache loader can't be null");
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
  public <K, V> VersionedOffHeapCacheBuilder evictionListener(
      final EvictionListener<K, V> evictionListener) {
    checkNotNull(evictionListener, "eviction listener can't be null");
    this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
    return this;
  }

  /**
   * Query executor.
   *
   * @param <K> the key type
   * @param <V> the value type
   * @param indexHandler the query executor
   * @return the versioned off heap cache builder
   */
  @SuppressWarnings("unchecked")
  public <K, V> VersionedOffHeapCacheBuilder indexHandler(final IndexHandler<K, V> indexHandler) {
    checkNotNull(indexHandler, "index handler can't be null");
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
  public VersionedOffHeapCacheBuilder concurrencyLevel(final int concurrencyLevel) {
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
  public VersionedOffHeapCacheBuilder evictionPeriod(final long evictionPeriod) {
    checkNotNull(evictionPeriod, "eviction period must be positive");
    this.evictionPeriod = evictionPeriod;
    return this;
  }

  /**
   * Buffer cleaner period.
   *
   * @param bufferCleanerPeriod the buffer cleaner period
   * @return the off heap cache builder
   */
  public VersionedOffHeapCacheBuilder bufferCleanerPeriod(final long bufferCleanerPeriod) {
    checkNotNull(bufferCleanerPeriod, "buffer cleaner period must be positive");
    this.bufferCleanerPeriod = bufferCleanerPeriod;
    return this;
  }

  /**
   * Buffer cleaner threshold.
   *
   * @param bufferCleanerThreshold the buffer cleaner threshold
   * @return the off heap cache builder
   */
  public VersionedOffHeapCacheBuilder bufferCleanerThreshold(final float bufferCleanerThreshold) {
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
  public synchronized VersionedOffHeapCacheBuilder addIndex(final String attributeName,
      final IndexType indexType) {
    handleIndex(attributeName, indexType);
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
  public <K, V> VersionedOffHeapCache build() {
    if (this.byteBufferStore == null) {
      throw new NecessaryArgumentException("ByteBufferStore must be set!");
    }
    return new VersionedOffHeapCache<>(name, byteBufferStore,
        (Serializer<V>) serializer, (CacheLoader<K, V>) cacheLoader,
        (EvictionListener<K, V>) evictionListener, (IndexHandler<K, V>) indexHandler,
        bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
  }

  /**
   * Builds the cache.
   *
   * @return the cache
   */
  @SuppressWarnings("unchecked")
  public VersionedOffHeapCache build(final String name) {
    return name(name).build();
  }

}
