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
package com.cetsoft.imcache.spring;

import com.cetsoft.imcache.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * The Class ImcacheCache.
 */
public class ImcacheCache implements org.springframework.cache.Cache {

  /**
   * The cache.
   */
  private Cache<Object, Object> cache;

  /**
   * Instantiates a new imcache cache.
   *
   * @param cache the cache
   */
  @SuppressWarnings("unchecked")
  public ImcacheCache(final Cache<Object, Object> cache) {
    this.cache = cache;
  }


  public String getName() {
    return cache.getName();
  }


  public Object getNativeCache() {
    return cache;
  }


  public ValueWrapper get(Object key) {
    if (key == null) {
      return null;
    }
    final Object value = cache.get(key);
    return value != null ? new SimpleValueWrapper(value) : null;
  }


  public void put(Object key, Object value) {
    cache.put(key, value);
  }


  public void evict(Object key) {
    cache.invalidate(key);
  }


  public void clear() {
    cache.clear();
  }


  @SuppressWarnings("unchecked")
  public <T> T get(Object key, Class<T> clazz) {
    if (key == null) {
      return null;
    }
    return (T) cache.get(key);
  }

}
