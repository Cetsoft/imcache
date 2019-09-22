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

import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotEmpty;
import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotNull;
import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkPositive;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.heap.HeapCache;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class HeapCacheBuilder.
 */
public class HeapCacheBuilder extends BaseCacheBuilder {

  /**
   * Cache number
   */
  private final AtomicInteger cacheNumber = new AtomicInteger();
  /**
   * The capacity.
   */
  private int capacity = 10000;
  /**
   * Expiry time unit
   */
  private TimeUnit expiryUnit = TimeUnit.MILLISECONDS;
  /**
   * Expiry in long
   */
  private long expiry = 5680281600L; //2150

  /**
   * Instantiates a new Heap cache builder.
   */
  public HeapCacheBuilder() {
    this.name = "imcache-heap-cache-" + cacheNumber.incrementAndGet();
  }

  /**
   * Time unit heap cache builder.
   *
   * @param expiryUnit the time unit
   * @return the heap cache builder
   */
  public HeapCacheBuilder expiryUnit(final TimeUnit expiryUnit) {
    checkNotNull(expiryUnit, "expiry unit can't be null");
    this.expiryUnit = expiryUnit;
    return this;
  }

  /**
   * Expiry heap cache builder.
   *
   * @param expiry the expiry
   * @return the heap cache builder
   */
  public HeapCacheBuilder expiry(final long expiry) {
    checkPositive(expiry, "expiry must be positive");
    this.expiry = expiry;
    return this;
  }

  /**
   * Name heap cache builder.
   *
   * @param name the name
   * @return the heap cache builder
   */
  public HeapCacheBuilder name(final String name) {
    checkNotEmpty(name, "name can't be empty");
    this.name = name;
    return this;
  }

  /**
   * Cache loader.
   *
   * @param <K> the key type
   * @param <V> the value type
   * @param cacheLoader the cache loader
   * @return the heap cache builder
   */
  @SuppressWarnings("unchecked")
  public <K, V> HeapCacheBuilder cacheLoader(final CacheLoader<K, V> cacheLoader) {
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
   * @return the heap cache builder
   */
  @SuppressWarnings("unchecked")
  public <K, V> HeapCacheBuilder evictionListener(final EvictionListener<K, V> evictionListener) {
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
   * @return the heap cache builder
   */
  @SuppressWarnings("unchecked")
  public synchronized <K, V> HeapCacheBuilder indexHandler(final IndexHandler<K, V> indexHandler) {
    checkNotNull(indexHandler, "index handler can't be null");
    this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
    isSearchable = true;
    return this;
  }

  /**
   * Capacity.
   *
   * @param capacity the capacity
   * @return the heap cache builder
   */
  public HeapCacheBuilder capacity(final int capacity) {
    checkPositive(expiry, "capacity must be positive");
    this.capacity = capacity;
    return this;
  }

  /**
   * Adds the index.
   *
   * @param attributeName the attribute name
   * @param indexType the index type
   * @return the heap cache builder
   */
  public synchronized HeapCacheBuilder addIndex(final String attributeName,
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
  public <K, V> HeapCache<K, V> build() {
    return new HeapCache<>(name, (CacheLoader<K, V>) cacheLoader,
        (EvictionListener<K, V>) evictionListener,
        (IndexHandler<K, V>) indexHandler, capacity, expiryUnit, expiry);
  }

  /**
   * Builds the cache.
   *
   * @param <K> the key type
   * @param <V> the value type
   * @return the cache
   */
  @SuppressWarnings("unchecked")
  public <K, V> HeapCache<K, V> build(final String name) {
    return name(name).build();
  }

}