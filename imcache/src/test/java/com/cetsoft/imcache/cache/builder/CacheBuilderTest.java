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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * The type Cache builder test.
 */
public class CacheBuilderTest {

  /**
   * Heap cache.
   */
  @Test
  public void heapCache() {
    assertTrue(CacheBuilder.heapCache() instanceof HeapCacheBuilder);
  }

  /**
   * Redis cache.
   */
  @Test
  public void redisCache() {
    assertTrue(CacheBuilder.redisCache() instanceof RedisCacheBuilder);
  }

  /**
   * Off heap cache.
   */
  @Test
  public void offHeapCache() {
    assertTrue(CacheBuilder.offHeapCache() instanceof OffHeapCacheBuilder);
  }

  /**
   * Versioned off heap cache.
   */
  @Test
  public void versionedOffHeapCache() {
    assertTrue(CacheBuilder.versionedOffHeapCache() instanceof VersionedOffHeapCacheBuilder);
  }

}
