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
package com.cetsoft.imcache.offheap;

import com.cetsoft.imcache.cache.AbstractSearchableCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.CacheStats;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.util.ThreadUtils;
import com.cetsoft.imcache.concurrent.ConcurrentCacheStats;
import com.cetsoft.imcache.concurrent.StripedReadWriteLock;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.offheap.bytebuffer.Pointer;
import com.cetsoft.imcache.serialization.Serializer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class OffHeapCache is a cache that uses offheap byte buffers to store or retrieve data by
 * serializing items into bytes. To do so, OffHeapCache uses pointers to point array location of an
 * item. OffHeapCache clears the buffers periodically to gain free space if buffers are dirty(unused
 * memory). It also does eviction depending on access time to the objects.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class OffHeapCache<K, V> extends AbstractSearchableCache<K, V> {

  /**
   * The default buffer cleaner period which is 10 minutes.
   */
  public static final long DEFAULT_BUFFER_CLEANER_PERIOD = 10 * 60 * 1000;
  /**
   * The default eviction period which is 10 minutes.
   */
  public static final long DEFAULT_EVICTION_PERIOD = 10 * 60 * 1000;
  /**
   * The default buffer cleaner threshold.
   */
  public static final float DEFAULT_BUFFER_CLEANER_THRESHOLD = 0.5f;
  /**
   * The Constant DEFAULT_CONCURRENCY_LEVEL.
   */
  public static final int DEFAULT_CONCURRENCY_LEVEL = 4;
  /**
   * The Constant DELTA.
   */
  private static final float DELTA = 0.00001f;
  /**
   * The Constant NO_OF_CLEANERS.
   */
  private static final AtomicInteger NO_OF_CLEANERS = new AtomicInteger();
  /**
   * The Constant NO_OF_EVICTORS.
   */
  private static final AtomicInteger NO_OF_EVICTORS = new AtomicInteger();
  private final ConcurrentCacheStats stats = new ConcurrentCacheStats();
  /**
   * Eviction Period
   */
  private final long evictionPeriod;
  /**
   * The pointer map.
   */
  protected ConcurrentMap<K, Pointer> pointerMap = new ConcurrentHashMap<K, Pointer>();
  /**
   * The serializer.
   */
  private Serializer<V> serializer;
  /**
   * The buffer store.
   */
  private OffHeapByteBufferStore bufferStore;
  /**
   * The read write lock.
   */
  private StripedReadWriteLock readWriteLock;

  /**
   * Instantiates a new offheap cache.
   *
   * @param name the name
   * @param cacheLoader the cache loader
   * @param evictionListener the eviction listener
   * @param indexHandler the query executor
   * @param byteBufferStore the byte buffer store
   * @param serializer the serializer
   * @param bufferCleanerPeriod the buffer cleaner period
   * @param bufferCleanerThreshold the buffer cleaner threshold
   * @param concurrencyLevel the concurrency level
   * @param evictionPeriod the eviction period
   */
  public OffHeapCache(final String name, final CacheLoader<K, V> cacheLoader,
      final EvictionListener<K, V> evictionListener,
      final IndexHandler<K, V> indexHandler, final OffHeapByteBufferStore byteBufferStore,
      final Serializer<V> serializer,
      final long bufferCleanerPeriod, final float bufferCleanerThreshold,
      final int concurrencyLevel,
      final long evictionPeriod) {
    super(name, cacheLoader, evictionListener, indexHandler);
    this.evictionPeriod = evictionPeriod;
    initCache(byteBufferStore, serializer, bufferCleanerPeriod, bufferCleanerThreshold,
        concurrencyLevel);
  }

  /**
   * Inits the cache.
   *
   * @param byteBufferStore the byte buffer store
   * @param serializer the serializer
   * @param bufferCleanerPeriod the buffer cleaner period
   * @param bufferCleanerThreshold the buffer cleaner threshol
   * @param concurrencyLevel the concurrency level
   */
  protected void initCache(OffHeapByteBufferStore byteBufferStore, Serializer<V> serializer,
      long bufferCleanerPeriod, final float bufferCleanerThreshold, int concurrencyLevel) {
    if (concurrencyLevel > 11 || concurrencyLevel < 0) {
      throw new IllegalArgumentException("ConcurrencyLevel must be between 0 and 11 inclusive!");
    }
    this.serializer = serializer;
    this.bufferStore = byteBufferStore;
    this.readWriteLock = new StripedReadWriteLock(concurrencyLevel);
    final ScheduledExecutorService cleanerService = Executors
        .newSingleThreadScheduledExecutor(runnable -> {
          String threadName = "imcache:bufferCleanerService(name=" + getName() + ",thread="
              + NO_OF_CLEANERS.incrementAndGet() + ")";
          return ThreadUtils.createDaemonThread(runnable, threadName);
        });
    cleanerService
        .scheduleAtFixedRate(() -> cleanBuffers(bufferCleanerThreshold), bufferCleanerPeriod,
            bufferCleanerPeriod, TimeUnit.MILLISECONDS);
    final ScheduledExecutorService evictionService = Executors
        .newSingleThreadScheduledExecutor(runnable -> {
          String threadName = "imcache:evictionService(name=" + getName() + ",thread="
              + NO_OF_EVICTORS.incrementAndGet() + ")";
          return ThreadUtils.createDaemonThread(runnable, threadName);
        });
    evictionService.scheduleAtFixedRate(() -> doEviction(), bufferCleanerPeriod, evictionPeriod,
        TimeUnit.MILLISECONDS);
  }


  public void put(K key, V value) {
    put(key, value, TimeUnit.MILLISECONDS, evictionPeriod * 3);
  }

  @Override
  public void put(final K key, final V value, final TimeUnit timeUnit, final long duration) {
    final long expiry = System.currentTimeMillis() + timeUnit.toMillis(duration);
    Pointer pointer = pointerMap.get(key);
    writeLock(key);
    try {
      final byte[] bytes = serializer.serialize(value);
      if (pointer == null) {
        pointer = bufferStore.store(bytes, expiry);
      } else {
        pointer = bufferStore.update(pointer, bytes, expiry);
      }
      pointerMap.put(key, pointer);
      indexHandler.add(key, value);
    } finally {
      writeUnlock(key);
    }
  }


  public V get(K key) {
    final Pointer pointer = pointerMap.get(key);
    if (pointer != null) {
      readLock(key);
      try {
        stats.incrementHitCount();
        final byte[] payload = bufferStore.retrieve(pointer);
        return serializer.deserialize(payload);
      } finally {
        readUnlock(key);
      }
    } else {
      stats.incrementMissCount();
      final V value = cacheLoader.load(key);
      if (value != null) {
        stats.incrementLoadCount();
        put(key, value);
      }
      return value;
    }
  }


  public V invalidate(K key) {
    writeLock(key);
    try {
      final Pointer pointer = pointerMap.get(key);
      if (pointer != null) {
        final byte[] payload = bufferStore.remove(pointer);
        pointerMap.remove(key);
        final V value = serializer.deserialize(payload);
        indexHandler.remove(key, value);
        return value;
      }
    } finally {
      writeUnlock(key);
    }
    return null;
  }


  public boolean contains(K key) {
    return pointerMap.containsKey(key);
  }


  public void clear() {
    synchronized (this) {
      pointerMap.clear();
      bufferStore.free();
    }
  }

  @Override
  public long size() {
    return pointerMap.size();
  }

  @Override
  public CacheStats stats() {
    return stats;
  }

  /**
   * Read Lock for key is locked.
   *
   * @param key the key
   */
  protected void readLock(K key) {
    readWriteLock.readLock(Math.abs(key.hashCode()));
  }

  /**
   * Read Lock for key is unlocked.
   *
   * @param key the key
   */
  protected void readUnlock(K key) {
    readWriteLock.readUnlock(Math.abs(key.hashCode()));
  }

  /**
   * Write Lock for key is locked..
   *
   * @param key the key
   */
  protected void writeLock(K key) {
    readWriteLock.writeLock(Math.abs(key.hashCode()));
  }

  /**
   * Write Lock for key is unlocked.
   *
   * @param key the key
   */
  protected void writeUnlock(K key) {
    readWriteLock.writeUnlock(Math.abs(key.hashCode()));
  }

  /**
   * Clean buffers.
   *
   * @param bufferCleanerThreshold the buffer cleaner threshold
   */
  protected void cleanBuffers(final float bufferCleanerThreshold) {
    // Buffers can be fully dirty and we may not find any pointer to resolve that.
    // This case is so unlikely that we did not consider.
    final Set<Entry<K, Pointer>> pointers = pointerMap.entrySet();
    final Map<OffHeapByteBuffer, Float> buffers = new HashMap<>();
    final Set<Integer> buffersToBeCleaned = new HashSet<Integer>();
    // For all pointers we try to understand if there is a need for redistribution.
    for (final Entry<K, Pointer> pointerEntry : pointers) {
      Float ratio = buffers.get(pointerEntry.getValue().getOffHeapByteBuffer());
      if (ratio == null) {
        // calculate the ratio of the dirty
        ratio = getDirtyRatio(pointerEntry.getValue());
        buffers.put(pointerEntry.getValue().getOffHeapByteBuffer(), ratio);
      }
      if (ratio - bufferCleanerThreshold > DELTA) {
        buffersToBeCleaned.add(pointerEntry.getValue().getOffHeapByteBuffer().getIndex());
        writeLock(pointerEntry.getKey());
        try {
          final byte[] payload = bufferStore.retrieve(pointerEntry.getValue());
          final Pointer newPointer = bufferStore
              .store(payload, pointerEntry.getValue().getExpiry());
          pointerMap.put(pointerEntry.getKey(), newPointer);
        } finally {
          writeUnlock(pointerEntry.getKey());
        }
      }
    }
    for (final int bufferIndex : buffersToBeCleaned) {
      bufferStore.free(bufferIndex);
    }
  }

  /**
   * Gets the dirty ratio.
   *
   * @param pointer the pointer
   * @return the dirty ratio
   */
  protected float getDirtyRatio(final Pointer pointer) {
    return (float) ((double) pointer.getOffHeapByteBuffer().dirtyMemory() / (
        pointer.getOffHeapByteBuffer()
            .freeMemory() + pointer.getOffHeapByteBuffer().usedMemory() + pointer
            .getOffHeapByteBuffer()
            .dirtyMemory()));
  }

  /**
   * Do eviction.
   */
  protected void doEviction() {
    final Set<Entry<K, Pointer>> entries = pointerMap.entrySet();
    for (final Entry<K, Pointer> entry : entries) {
      if (entry.getValue().isExpired()) {
        final V value = invalidate(entry.getKey());
        evictionListener.onEviction(entry.getKey(), value);
        stats.incrementEvictionCount();
      }
    }
  }

}
