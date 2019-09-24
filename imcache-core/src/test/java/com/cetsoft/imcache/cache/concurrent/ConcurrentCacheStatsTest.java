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
