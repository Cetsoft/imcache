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
package com.cetsoft.imcache.heap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * The Class OffHeapCacheTest.
 */
public class HeapCacheTest {

  /**
   * The cache loader.
   */
  @Mock
  CacheLoader<String, String> cacheLoader;

  /**
   * The eviction listener.
   */
  @Mock
  EvictionListener<String, String> evictionListener;

  /**
   * The index handler.
   */
  @Mock
  IndexHandler<String, String> indexHandler;

  /**
   * The cache.
   */
  HeapCache<String, String> cache;

  /**
   * Setup.
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    cache = spy(
        new HeapCache<>("simple-cache", cacheLoader, evictionListener, indexHandler, 100,
            TimeUnit.SECONDS,
            10000));
  }

  /**
   * Put.
   */
  @Test
  public void putAndGet() {
    cache.put("a", "b");

    assertEquals(cache.get("a"), "b");
    verify(indexHandler).add("a", "b");
  }

  /**
   * Put.
   */
  @Test
  public void putAndGetWithTimeout() throws InterruptedException {
    cache.put("a", "b", TimeUnit.MILLISECONDS, 0);

    Thread.sleep(2);

    assertEquals(cache.get("a"), null);
    verify(indexHandler).add("a", "b");
  }

  /**
   * Get.
   */
  @Test
  public void getLoadsData() {
    doReturn("d").when(cacheLoader).load("c");

    assertEquals(cache.get("c"), "d");
    verify(indexHandler).add("c", "d");
  }

  @Test
  public void invalidate() {
    cache.put("a", "b");
    cache.invalidate("a");

    verify(indexHandler).add("a", "b");
    assertEquals(cache.get("a"), null);
  }


  /**
   * Contains.
   */
  @Test
  public void contains() {
    cache.put("a", "b");

    assertTrue(cache.contains("a"));
  }

  @Test
  public void clear() {
    cache.clear();

    assertEquals(0, cache.size());
  }

  @Test
  public void size() {
    cache.put("a", "a");
    cache.put("b", "a");
    cache.put("c", "a");
    cache.put("c", "a");

    assertEquals(cache.size(), 3);
  }

}
