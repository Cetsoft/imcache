/*
 * Copyright (C) 2015 Cetsoft, http://www.cetsoft.com
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
 * Date   : Nov 8, 2013
 */
package com.cetsoft.imcache.cache.search.criteria;

import com.cetsoft.imcache.cache.search.index.CacheIndex;
import java.util.List;

/**
 * The Class ArithmeticCriteria is used for arithmetic operations.
 */
public abstract class ArithmeticCriteria extends LogicalCriteria {

  /**
   * The expected value.
   */
  protected Object value;
  /**
   * The attribute name.
   */
  private String attributeName;

  /**
   * Instantiates a new equals to criteria.
   *
   * @param attributeName the attribute name
   * @param value the expected value
   */
  public ArithmeticCriteria(String attributeName, Object value) {
    this.attributeName = attributeName;
    this.value = value;
  }

  /**
   * Gets the attribute name.
   *
   * @return the attribute name
   */
  public String getAttributeName() {
    return attributeName;
  }

  /**
   * Meets the given criteria.
   *
   * @param cacheIndex the cache index
   * @return the list
   */
  public abstract List<Object> meets(CacheIndex cacheIndex);

}
