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
package com.cetsoft.imcache.offheap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.serialization.Serializer;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * The Class OffHeapCacheTest.
 */
public class OffHeapCacheTest {

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
   * The serializer.
   */
  Serializer<String> serializer = new Serializer<String>() {

    @Override
    public byte[] serialize(String value) {
      return value.getBytes();
    }

    @Override
    public String deserialize(byte[] payload) {
      return new String(payload);
    }
  };

  /**
   * The buffer store.
   */
  @Spy
  OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(1000, 2);

  /**
   * The cache.
   */
  OffHeapCache<String, String> cache;

  /**
   * Setup.
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    cache = spy(
        new OffHeapCache<>("offheap", cacheLoader, evictionListener, indexHandler, bufferStore,
            serializer, 100000000, 05f, 1, 100000000));
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
    verify(bufferStore).free();
  }

  @Test
  public void size() {
    cache.put("a", "a");
    cache.put("b", "a");
    cache.put("c", "a");
    cache.put("c", "a");

    assertEquals(cache.size(), 3);
  }

  /**
   * Do eviction.
   */
  @Test
  public void doEviction() {
    cache.put("a", "b", TimeUnit.MILLISECONDS, 0);
    cache.put("c", "d", TimeUnit.MILLISECONDS, 0);

    try {
      Thread.sleep(3);
    } catch (InterruptedException e) {
    }
    cache.doEviction();

    verify(evictionListener).onEviction("a", "b");
    verify(evictionListener).onEviction("c", "d");
    assertEquals(cache.size(), 0);
  }

  /**
   * Clean buffers.
   */
  @Test
  @SuppressWarnings("unchecked")
  public void cleanBuffers() {
    cache.put("a", "b");
    cache.put("a", "ab");
    cache.put("c", "d");
    cache.put("c", "cd");

    cache.cleanBuffers(0.01f);

    verify(bufferStore, times(2)).retrieve(any());
  }

  /**
   * Inits the throws exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void initThrowsException() {
    cache.initCache(bufferStore, serializer, 0, 0, -12);
  }

  /**
   * Inits the.
   */
  @Test
  public void init() {
    cache.initCache(bufferStore, serializer, 1, 1, 1);
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
    }
    verify(cache, atLeast(1)).cleanBuffers(1);
    verify(cache, atLeast(1)).doEviction();
  }
}
