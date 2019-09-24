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
package com.cetsoft.imcache.cache;

/**
 * The Interface VersionedItem provides versioning for the cached items. Implementations of this
 * class must be immutable.
 *
 * @param <V> the value type
 */
public interface VersionedItem<V> extends CacheItem<V> {

  /**
   * Gets the version.
   *
   * @return the version
   */
  int getVersion();

  /**
   * Returns value
   *
   * @return value
   */
  @Override
  V getValue();

  /*
   * Returns a new VersionedItem instance by incrementing version
   */
  VersionedItem<V> update(V newValue);
}
