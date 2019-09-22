/**
 * Copyright © 2013 Cetsoft. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cetsoft.imcache.cache.builder;

/**
 * The Class CacheBuilder.
 */
public class CacheBuilder {

  /**
   * Heap cache.
   *
   * @return the heap cache builder
   */
  public static HeapCacheBuilder heapCache() {
    return new HeapCacheBuilder();
  }

  /**
   * Off heap cache.
   *
   * @return the off heap cache builder
   */
  public static OffHeapCacheBuilder offHeapCache() {
    return new OffHeapCacheBuilder();
  }

  /**
   * Versioned Off heap cache.
   *
   * @return the off heap cache builder
   */
  public static VersionedOffHeapCacheBuilder versionedOffHeapCache() {
    return new VersionedOffHeapCacheBuilder();
  }

  /**
   * Redis cache.
   *
   * @return the redis cache builder
   */
  public static RedisCacheBuilder redisCache() {
    return new RedisCacheBuilder();
  }

}
