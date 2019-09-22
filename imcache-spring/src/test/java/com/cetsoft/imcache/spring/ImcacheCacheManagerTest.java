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
package com.cetsoft.imcache.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.builder.BaseCacheBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ImcacheCacheManagerTest {

  @Mock
  BaseCacheBuilder builder;

  @Mock
  SearchableCache<Object, Object> cache;

  ImcacheCacheManager cacheManager;

  String cacheName = "cache";

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    doReturn(cache).when(builder).build(cacheName);
    cacheManager = new ImcacheCacheManager(builder);
  }

  @Test
  public void getCache() {
    assertEquals(cache, cacheManager.getCache(cacheName).getNativeCache());
  }

  @Test
  public void getCacheNames() {
    cacheManager.getCache(cacheName);
    assertEquals(1, cacheManager.getCacheNames().size());
    assertTrue(cacheManager.getCacheNames().contains(cacheName));
  }

  @Test
  public void setGetCacheBuilder() {
    cacheManager = new ImcacheCacheManager();
    cacheManager.setCacheBuilder(builder);
    assertEquals(builder, cacheManager.getCacheBuilder());
  }

}
