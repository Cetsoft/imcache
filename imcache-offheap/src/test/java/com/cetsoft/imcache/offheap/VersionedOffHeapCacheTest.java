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
package com.cetsoft.imcache.offheap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SimpleItem;
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
public class VersionedOffHeapCacheTest {

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
  VersionedOffHeapCache<String, String> cache;

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
   * Setup.
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    cache = spy(new VersionedOffHeapCache<String, String>("versioned-offheap",
            bufferStore, serializer, cacheLoader, evictionListener, indexHandler,
            100000L, 0.5f, 1, 100000L));
    cache.offHeapCache = spy(cache.offHeapCache);
  }

  /**
   * Put.
   */
  @Test
  public void put() {
    final String key = "key1", value = "valueA";

    cache.put(key, new SimpleItem(value));

    verify(cache.offHeapCache).put(key, new SimpleItem<>(0, value));
  }

  /**
   * Put.
   */
  @Test
  public void putWithTimeout() throws InterruptedException {
    final String key = "key1", value = "valueA";

    cache.put(key, new SimpleItem(value), TimeUnit.MILLISECONDS, 1000);

    assertEquals(new SimpleItem<>(0, value), cache.get(key));
  }

  /**
   * contains.
   */
  @Test
  public void contains() throws InterruptedException {
    final String key = "key11", value = "valueA";

    cache.put(key, new SimpleItem(value), TimeUnit.MILLISECONDS, 1000);

    assertTrue(cache.contains(key));
  }

  /**
   * invalidate.
   */
  @Test
  public void invalidate() throws InterruptedException {
    final String key = "key12", value = "valueA";

    cache.put(key, new SimpleItem(value), TimeUnit.MILLISECONDS, 1000);
    cache.invalidate(key);

    assertEquals(null, cache.get(key));
  }


  /**
   * size.
   */
  @Test
  public void size() throws InterruptedException {
    final int size = 5;
    for (int i = 0; i < 5; i++) {
      cache.put( "key|" + i, new SimpleItem(i +""));
    }
    assertEquals(size, cache.size());
    cache.clear();
    assertEquals(0, cache.size());
  }

  /**
   * Put version are not same.
   */
  @Test(expected = StaleItemException.class)
  public void putVersionAreNotSame() {
    final String key = "key2", value = "valueA";

    cache.put(key, new SimpleItem(2, value));
    cache.put(key, new SimpleItem(value));
  }

  /**
   * Put version are not same.
   */
  @Test
  public void getLoadsData() {
    final String key = "keyLoading", value = "valueA";

    doReturn(value).when(cacheLoader).load(key);

    assertEquals(new SimpleItem<>(0, value), cache.get(key));
  }

  /**
   * Put version race condition.
   */
  @Test(expected = StaleItemException.class)
  public void putVersionRaceCondition() {
    final String key = "key3", value = "valueA";

    doReturn(null).doReturn(new SimpleItem<>(2, value)).when(cache).get(key);

    cache.put(key, new SimpleItem<>(value));
  }
}
