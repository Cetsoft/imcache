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
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.populator.ConcurrentCachePopulator;
import com.cetsoft.imcache.cache.util.CacheUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class CachePopulatorExample.
 */
public class CachePopulatorExample extends ConcurrentCachePopulator<String, String> {

  /**
   * Instantiates a new cache populator example.
   *
   * @param cache the cache
   */
  public CachePopulatorExample(Cache<String, String> cache) {
    super(cache);
  }

  public static void example() {
    Cache<String, String> cache = CacheBuilder.heapCache().build();
    CachePopulatorExample populatorExample = new CachePopulatorExample(cache);
    populatorExample.pupulate();
    System.out.println(cache.get("0"));
  }

  public static void main(String[] args) {
    example();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.cache.CachePopulator#loadEntries()
   */
  public List<CacheEntry<String, String>> loadEntries() {
    final int SIZE = 3;
    List<CacheEntry<String, String>> cacheEntries = new ArrayList<CacheEntry<String, String>>(SIZE);
    for (int i = 0; i < SIZE; i++) {
      cacheEntries.add(CacheUtils.createEntry("" + i, "" + i));
    }
    return cacheEntries;
  }

}
