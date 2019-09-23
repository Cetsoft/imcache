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
package com.cetsoft.imcache.cache.async;

import com.cetsoft.imcache.cache.util.ThreadUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The basic eviction listener interface for receiving eviction events. When eviction occurs, this
 * class creates a thread to save the data.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class BasicEvictionListener<K, V> implements AsyncEvictionListener<K, V> {

  /**
   * The Constant NO_OF_EVICTION_LISTENERS.
   */
  private static final AtomicInteger NO_OF_EVICTION_LISTENERS = new AtomicInteger();
  private final ExecutorService asyncEvictionExecutor;

  /**
   * Instantiates a new Basic eviction listener.
   */
  public BasicEvictionListener() {
    this.asyncEvictionExecutor = Executors.newSingleThreadExecutor(
        runnable -> ThreadUtils
            .createDaemonThread(runnable, "imcache:basicAsyncEvictionListener(thread="
                + NO_OF_EVICTION_LISTENERS.incrementAndGet() + ")"));
  }


  public void onEviction(final K key, final V value) {
    asyncEvictionExecutor.execute(() -> save(key, value));
  }

  /**
   * Saves the key value pair.
   *
   * @param key the key
   * @param value the value
   */
  public abstract void save(K key, V value);

}
