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

import java.util.Objects;

/**
 * The Class SimpleItem is a basic implementation of a versionedItem.
 *
 * @param <V> the value type
 */
public class SimpleItem<V> implements VersionedItem<V> {

  /**
   * The version.
   */
  private final int version;

  /**
   * The value.
   */
  private final V value;

  /**
   * Instantiates a new simple cache item.
   *
   * @param value the value
   */
  public SimpleItem(final V value) {
    this.value = value;
    this.version = -1;
  }

  /**
   * Instantiates a new simple cache item.
   *
   * @param version the version
   * @param value the value
   */
  public SimpleItem(final int version, final V value) {
    this.version = version;
    this.value = value;
  }


  public V getValue() {
    return value;
  }


  public int getVersion() {
    return version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SimpleItem)) {
      return false;
    }
    SimpleItem<?> that = (SimpleItem<?>) o;
    return getVersion() == that.getVersion() &&
        Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getVersion(), getValue());
  }

  @Override
  public String toString() {
    return "SimpleItem{" + "version=" + version + ", value=" + value + '}';
  }
}
