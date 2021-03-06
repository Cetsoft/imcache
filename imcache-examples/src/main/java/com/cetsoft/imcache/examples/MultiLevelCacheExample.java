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
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;

/**
 * The Class MultiLevelCacheExample.
 */
public class MultiLevelCacheExample {

  @SuppressWarnings("null")
  public static void example() {
    final CacheDao cacheDao = null;// This is just for example purposes.
    OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(10000, 10);
    final Cache<String, String> offHeapCache = CacheBuilder.offHeapCache().storage(bufferStore)
        .cacheLoader((CacheLoader<String, String>) key -> cacheDao.load(key)).evictionListener(
            (EvictionListener<String, String>) (key, value) -> cacheDao.store(key, value)).build();
    Cache<String, String> multiLevelCache = CacheBuilder.heapCache().cacheLoader(
        (CacheLoader<String, String>) key -> offHeapCache.get(key)).evictionListener(
        (EvictionListener<String, String>) (key, value) -> offHeapCache.put(key, value))
        .capacity(10000).build();
    multiLevelCache.put("red", "apple");
  }

  public static void main(String[] args) {
    example();
  }

  /**
   * The Interface CacheDao.
   */
  public interface CacheDao {

    /**
     * Load.
     *
     * @param key the key
     * @return the string
     */
    String load(String key);

    /**
     * Store.
     *
     * @param key the key
     * @param value the value
     */
    void store(String key, String value);
  }
}
