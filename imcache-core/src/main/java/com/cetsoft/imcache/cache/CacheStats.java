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
  long getRequestCount();

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
