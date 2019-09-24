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
  public long getRequestCount() {
    return hitCount.longValue() + missCount.longValue();
  }

  @Override
  public double hitRate() {
    return hitCount.doubleValue() / getRequestCount();
  }

  @Override
  public double missRate() {
    return missCount.doubleValue() / getRequestCount();
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
