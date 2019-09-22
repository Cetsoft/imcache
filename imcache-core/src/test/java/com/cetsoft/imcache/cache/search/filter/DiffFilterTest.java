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
 * Date   : Jun 1, 2014
 */
package com.cetsoft.imcache.cache.search.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * The Class DiffFilterTest.
 */
@SuppressWarnings("unchecked")
public class DiffFilterTest {

  /**
   * The diff filter.
   */
  DiffFilter diffFilter;

  /**
   * The filter.
   */
  @Mock
  Filter filter;

  /**
   * Setup.
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    diffFilter = spy(new DiffFilter(filter, filter));
  }

  /**
   * Filter.
   */
  @Test
  public void filter() {
    Object object1 = new Object();
    Object object2 = new Object();
    Object object3 = new Object();
    List<Object> list1 = new ArrayList<Object>();
    list1.add(object1);
    list1.add(object2);
    List<Object> list2 = new ArrayList<Object>();
    list2.add(object2);
    list2.add(object3);
    doReturn(list1).doReturn(list2).when(filter).filter(anyList());
    List<Object> actualObjects = diffFilter.filter(list1);
    assertEquals(object1, actualObjects.get(0));
  }

}
