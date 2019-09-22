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
package com.cetsoft.imcache.cache.search;

import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.filter.Filter;

/**
 * The Interface Query is a piece of code (a query) that is sent to a cache in order to get
 * information back from the cache. It is used as the way of retrieving the information from cache.
 */
public interface Query {

  /**
   * Returns list of Criterias.
   *
   * @return the list
   */
  Criteria getCriteria();

  /**
   * Adds the criteria.
   *
   * @param criteria the criteria
   * @return the query
   */
  Query setCriteria(Criteria criteria);

  /**
   * Gets the filter.
   *
   * @return the filter
   */
  Filter getFilter();

  /**
   * Sets the filter.
   *
   * @param filter the filter
   * @return the query
   */
  Query setFilter(Filter filter);
}
