package com.cetsoft.imcache.concurrent;

import com.cetsoft.imcache.cache.CacheStats;
import java.util.concurrent.atomic.LongAdder;

/**
 * Thread safe implementation for cache stats
 */
public class ConcurrentCacheStats implements CacheStats {

  private static final LongAdder hitCount = new LongAdder();
  private static final LongAdder missCount = new LongAdder();
  private static final LongAdder loadCount = new LongAdder();
  private static final LongAdder evictionCount = new LongAdder();

  @Override
  public long getHitCount() {
    return hitCount.longValue();
  }

  @Override
  public long getMissCount() {
    return missCount.longValue();
  }

  @Override
  public long getLoadCount() {
    return loadCount.longValue();
  }

  @Override
  public long getEvictionCount() {
    return evictionCount.longValue();
  }

  @Override
  public long requestCount() {
    return hitCount.longValue() + missCount.longValue();
  }

  @Override
  public double hitRate() {
    return hitCount.doubleValue() / requestCount();
  }

  @Override
  public double missRate() {
    return missCount.doubleValue() / requestCount();
  }

  /**
   * Increment hit count.
   */
  public void incrementHitCount() {
    hitCount.increment();
  }

  /**
   * Increment miss count.
   */
  public void incrementMissCount() {
    missCount.increment();
  }

  /**
   * Increment load count.
   */
  public void incrementLoadCount() {
    loadCount.increment();
  }

  /**
   * Increment eviction count.
   */
  public void incrementEvictionCount() {
    evictionCount.increment();
  }
}
