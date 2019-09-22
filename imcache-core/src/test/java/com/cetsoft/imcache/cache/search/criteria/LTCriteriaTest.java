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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.cetsoft.imcache.cache.search.index.CacheIndex;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * The Class LTCriteriaTest.
 */
public class LTCriteriaTest {

  /**
   * The expected value.
   */
  @Mock
  Object expectedValue;

  /**
   * The result.
   */
  @Mock
  List<Object> result;

  /**
   * The cache index.
   */
  @Mock
  CacheIndex cacheIndex;

  /**
   * Setup.
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Meets.
   */
  @Test
  public void meets() {
    LTCriteria criteria = new LTCriteria("a", expectedValue);
    doReturn(result).when(cacheIndex).lessThan(expectedValue);
    List<Object> actualResult = criteria.meets(cacheIndex);
    verify(cacheIndex).lessThan(expectedValue);
    assertEquals(result, actualResult);
  }

}
