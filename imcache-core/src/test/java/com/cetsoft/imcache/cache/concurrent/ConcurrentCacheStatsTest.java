package com.cetsoft.imcache.cache.concurrent;

import static org.junit.Assert.assertEquals;

import com.cetsoft.imcache.concurrent.ConcurrentCacheStats;
import org.junit.Test;

public class ConcurrentCacheStatsTest {

  @Test
  public void cacheStats()
  {
    final int hits = 3, misses = 2, loads = 1, evictions = 2;
    final ConcurrentCacheStats cacheStats = new ConcurrentCacheStats();
    for (int i = 0; i < hits; i++) {
      cacheStats.incrementHitCount();
    }
    for (int i = 0; i < misses; i++) {
      cacheStats.incrementMissCount();
    }
    for (int i = 0; i < loads; i++) {
      cacheStats.incrementLoadCount();
    }
    for (int i = 0; i < evictions; i++) {
      cacheStats.incrementEvictionCount();
    }
    assertEquals(hits, cacheStats.getHitCount());
    assertEquals(misses, cacheStats.getMissCount());
    assertEquals(loads, cacheStats.getLoadCount());
    assertEquals(evictions, cacheStats.getEvictionCount());
    assertEquals(hits + misses, cacheStats.getRequestCount());
    assertEquals((double) hits/cacheStats.getRequestCount(), cacheStats.hitRate(), 0.001);
    assertEquals((double) misses/cacheStats.getRequestCount(), cacheStats.missRate(), 0.001);
  }
}
