package com.cetsoft.imcache.heap;

import com.cetsoft.imcache.cache.AbstractSearchableCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.CacheStats;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.concurrent.ConcurrentCacheStats;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * The type Heap cache.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 */
public class HeapCache<K, V> extends AbstractSearchableCache<K, V> {

  private final com.github.benmanes.caffeine.cache.Cache<K, V> caffeine;
  private final ConcurrentCacheStats stats = new ConcurrentCacheStats();

  /**
   * Instantiates a new abstract cache.
   *
   * @param cacheLoader the cache loader
   * @param evictionListener the eviction listener
   * @param indexHandler the index handler
   * @param limit the limit
   * @param timeUnit the time unit
   * @param duration the duration
   */
  public HeapCache(final CacheLoader<K, V> cacheLoader,
      final EvictionListener<K, V> evictionListener, final IndexHandler<K, V> indexHandler,
      final long limit, final TimeUnit timeUnit, final long duration) {
    super(cacheLoader, evictionListener, indexHandler);
    this.caffeine = Caffeine.newBuilder()
        .maximumSize(limit)
        .expireAfterWrite(duration, timeUnit)
        .removalListener((key, value, cause) -> {
          evictionListener.onEviction((K)key, (V)value);
          stats.incrementEvictionCount();
        })
        .build();
  }

  @Override
  public void put(final K key, final V value) {
    caffeine.put(key, value);
    indexHandler.add(key, value);
  }

  @Override
  public void put(final K key, final V value, final TimeUnit timeUnit, final long duration) {
    caffeine.policy().expireVariably().get().put(key, value, duration, timeUnit);
    indexHandler.add(key, value);
  }

  @Override
  public V get(final K key) {
    V value = caffeine.getIfPresent(key);
    if (value != null) {
      stats.incrementHitCount();
      return value;
    }
    stats.incrementMissCount();
    //Explicitly not locking at the cost of loading item once more
    if (cacheLoader != null) {
      value = cacheLoader.load(key);
      if (value != null) {
        this.put(key, value);
        stats.incrementLoadCount();
        return value;
      }
    }
    return null;
  }

  @Override
  public V invalidate(final K key) {
    V value = caffeine.getIfPresent(key);
    caffeine.invalidate(key);
    indexHandler.remove(key, value);
    return value;
  }

  @Override
  public boolean contains(final K key) {
    return caffeine.getIfPresent(key) != null;
  }

  @Override
  public void clear() {
    caffeine.cleanUp();
    indexHandler.clear();
  }

  @Override
  public long size() {
    return caffeine.estimatedSize();
  }

  @Override
  public CacheStats stats() {
    return stats;
  }
}
