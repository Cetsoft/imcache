package com.cetsoft.imcache.cache;

/**
 * The interface CacheStats
 *
 * The interface gives statistics for the cache.
 */
public interface CacheStats {

  /**
   * Number of times cache was hit
   *
   * @return cache hit count
   */
  long getHitCount();

  /**
   * Number of times cache was missed
   *
   * @return cache miss count
   */
  long getMissCount();

  /**
   * Number of times cache was loaded
   *
   * @return cache load count
   */
  long getLoadCount();

  /**
   * Number of times cache was evicted
   *
   * @return eviction count
   */
  long getEvictionCount();

  /**
   * Number of times cache was requested
   *
   * @return request count
   */
  long requestCount();

  /**
   * The hit rate
   *
   * @return hit rate
   */
  double hitRate();

  /**
   * The miss rate
   *
   * @return miss rate
   */
  double missRate();
}
