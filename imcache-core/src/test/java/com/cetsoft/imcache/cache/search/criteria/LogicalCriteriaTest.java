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
package com.cetsoft.imcache.cache.search.criteria;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mock;

public class LogicalCriteriaTest {

  @Mock
  Criteria criteria;

  @Test
  public void logicalCriteria() {
    BetweenCriteria betweenCriteria = new BetweenCriteria("value", 3, 10);
    assertTrue(betweenCriteria.or(criteria) instanceof OrCriteria);
    assertTrue(betweenCriteria.and(criteria) instanceof AndCriteria);
    assertTrue(betweenCriteria.diff(criteria) instanceof DiffCriteria);
  }
}
