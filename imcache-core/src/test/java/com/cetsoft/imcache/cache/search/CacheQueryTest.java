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

import static org.junit.Assert.assertEquals;

import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.criteria.ETCriteria;
import com.cetsoft.imcache.cache.search.filter.Filter;
import com.cetsoft.imcache.cache.search.filter.LTFilter;
import org.junit.Test;

public class CacheQueryTest {

  @Test
  public void cacheQuery() {
    Criteria etCriteria = new ETCriteria("id", 3);
    Filter ltFilter = new LTFilter("age", 18);
    Query query = CacheQuery.newQuery().setCriteria(etCriteria).setFilter(ltFilter);
    assertEquals(etCriteria, query.getCriteria());
    assertEquals(ltFilter, query.getFilter());
  }
}
