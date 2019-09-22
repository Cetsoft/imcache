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
 * Date   : Jun 2, 2014
 */
package com.cetsoft.imcache.cache.search.criteria;

import static org.junit.Assert.assertEquals;

import com.cetsoft.imcache.cache.search.index.CacheIndex;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * The Class AndCriteriaTest.
 */
public class AndCriteriaTest {

  /**
   * The criteria.
   */
  @Mock
  Criteria criteria;

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
   * Gets the criterias.
   *
   * @return the criterias
   */
  @Test
  public void getCriterias() {
    AndCriteria andCriteria = new AndCriteria(criteria, criteria);
    Criteria[] criterias = andCriteria.getCriterias();
    assertEquals(criteria, criterias[0]);
  }

}
