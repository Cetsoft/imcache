/*
 * Copyright (C) 2014 Yusuf Aytas, http://www.yusufaytas.com
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
 *
 * Author : Yusuf Aytas
 * Date   : Dec 29, 2013
 */
package com.cetsoft.imcache.spring;

import com.cetsoft.imcache.cache.builder.BaseCacheBuilder;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * The Class ImcacheCacheManager.
 */
public class ImcacheCacheManager implements CacheManager, InitializingBean {

  /**
   * The caches.
   */
  private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
  /**
   * The cache builder.
   */
  private BaseCacheBuilder cacheBuilder;

  /**
   * Instantiates a new imcache cache manager.
   */
  public ImcacheCacheManager() {
    this(CacheBuilder.heapCache());
  }

  /**
   * Instantiates a new imcache cache manager.
   *
   * @param cacheBuilder the cache builder
   */
  public ImcacheCacheManager(BaseCacheBuilder cacheBuilder) {
    this.cacheBuilder = cacheBuilder;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.cache.CacheManager#getCache(java.lang.String)
   */
  public Cache getCache(final String name) {
    return caches
        .computeIfAbsent(name, (cacheName) -> new ImcacheCache(cacheBuilder.build(cacheName)));
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.cache.CacheManager#getCacheNames()
   */
  public Collection<String> getCacheNames() {
    return Collections.unmodifiableCollection(caches.keySet());
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  public void afterPropertiesSet() throws Exception {

  }

  /**
   * Gets the cache builder.
   *
   * @return the cache builder
   */
  public BaseCacheBuilder getCacheBuilder() {
    return cacheBuilder;
  }

  /**
   * Sets the cache builder.
   *
   * @param cacheBuilder the new cache builder
   */
  public void setCacheBuilder(BaseCacheBuilder cacheBuilder) {
    this.cacheBuilder = cacheBuilder;
  }

}
