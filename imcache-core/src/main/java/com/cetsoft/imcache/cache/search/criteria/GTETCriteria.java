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
 * The Class GTETCriteria is used to retrieve items greater than equals to the given value.
 */
public class GTETCriteria extends ArithmeticCriteria {

  /**
   * Instantiates a new gTET criteria.
   *
   * @param attributeName the attribute name
   * @param value the value
   */
  public GTETCriteria(String attributeName, Object value) {
    super(attributeName, value);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.cache.search.criteria.Criteria#meets(com.cetsoft.
   * imcache.cache.search.index.CacheIndex)
   */
  public List<Object> meets(CacheIndex cacheIndex) {
    return cacheIndex.greaterThanOrEqualsTo(value);
  }

}
