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
package com.cetsoft.imcache.spring;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheCategory;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.builder.BaseCacheBuilder;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.offheap.OffHeapCache;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class SpringCacheBuilder.
 */
public class SpringCacheBuilder extends BaseCacheBuilder {

  /**
   * The type.
   */
  protected CacheCategory type;

  /**
   * The concurrency level.
   */
  private int concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;

  /**
   * The eviction period.
   */
  private long evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;

  /**
   * The buffer cleaner period.
   */
  private long bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;

  /**
   * The buffer cleaner threshold.
   */
  private float bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;

  /**
   * The buffer store.
   */
  private OffHeapByteBufferStore bufferStore;

  private String redisHost = "127.0.0.1";

  private int redisPort = 6379;

  private int heapCapacity = 10000;

  /**
   * Instantiates a new spring cache builder.
   */
  public SpringCacheBuilder() {
    this.type = CacheCategory.HEAP;
  }

  /**
   * Instantiates a new spring cache builder.
   *
   * @param type the type
   */
  public SpringCacheBuilder(String type) {
    super();
    this.type = CacheCategory.valueOf(type.toUpperCase());
  }


  public <K, V> Cache<K, V> build() {

    switch (this.type) {
      case OFFHEAP:
        return CacheBuilder.offHeapCache().cacheLoader(cacheLoader)
            .evictionListener(evictionListener)
            .indexHandler(indexHandler).concurrencyLevel(concurrencyLevel)
            .bufferCleanerPeriod(bufferCleanerPeriod).bufferCleanerThreshold(bufferCleanerThreshold)
            .evictionPeriod(evictionPeriod).serializer(serializer).storage(bufferStore).build();

      case VERSIONED_OFFHEAP:
        return CacheBuilder.versionedOffHeapCache().cacheLoader(cacheLoader)
            .evictionListener(evictionListener)
            .indexHandler(indexHandler).concurrencyLevel(concurrencyLevel)
            .bufferCleanerPeriod(bufferCleanerPeriod).bufferCleanerThreshold(bufferCleanerThreshold)
            .evictionPeriod(evictionPeriod).serializer(serializer).storage(bufferStore).build();

      case REDIS:
        return CacheBuilder.redisCache().cacheLoader(cacheLoader).evictionListener(evictionListener)
            .serializer(serializer).concurrencyLevel(concurrencyLevel).hostName(redisHost)
            .port(redisPort)
            .build();

      default:
        return CacheBuilder.heapCache().cacheLoader(cacheLoader).evictionListener(evictionListener)
            .indexHandler(indexHandler).build();
    }
  }


  public <K, V> Cache<K, V> build(final String name) {
    this.name = name;
    return build();
  }

  /**
   * Sets the type.
   *
   * @param type the new type
   */
  public void setType(String type) {
    this.type = CacheCategory.valueOf(type.toUpperCase());
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

  /**
   * Sets the REDIS server host.
   *
   * @param redisHost the redis host
   */
  public void setRedisHost(String redisHost) {
    this.redisHost = redisHost;
  }

  /**
   * Sets the REDIS server PORT.
   *
   * @param redisPort the redis port
   */
  public void setRedisPort(int redisPort) {
    this.redisPort = redisPort;
  }

  /**
   * Sets the HEAP capacity. If not set then it defaults to 10000.
   *
   * @param heapCapacity the heap capacity
   */
  public void setHeapCapacity(int heapCapacity) {
    this.heapCapacity = heapCapacity;
  }

}
