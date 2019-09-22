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
package com.cetsoft.imcache.cache.search.filter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mock;

public class LogicalFilterTest {

  @Mock
  Filter filter;

  @Test
  public void logicalFilter() {
    BetweenFilter betweenFilter = new BetweenFilter("value", 3, 10);
    assertTrue(betweenFilter.or(filter) instanceof OrFilter);
    assertTrue(betweenFilter.and(filter) instanceof AndFilter);
    assertTrue(betweenFilter.diff(filter) instanceof DiffFilter);
  }
}
