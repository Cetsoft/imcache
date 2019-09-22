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
package com.cetsoft.imcache.cache.populator;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * The Class LazyCachePopulatorTest.
 */
public class LazyCachePopulatorTest {

  /**
   * The cache.
   */
  @Mock
  Cache<Object, Object> cache;

  /**
   * The entry.
   */
  @Mock
  CacheEntry<Object, Object> entry;

  /**
   * The populator.
   */
  LazyCachePopulator<Object, Object> populator;

  /**
   * Setup.
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    populator = spy(new LazyCachePopulator<Object, Object>(cache) {
      public List<CacheEntry<Object, Object>> loadEntries() {
        return null;
      }
    });
  }

  /**
   * Populate.
   */
  @Test
  public void populate() {
    Object object = new Object();
    List<CacheEntry<Object, Object>> entries = new ArrayList<CacheEntry<Object, Object>>();
    entries.add(entry);
    doReturn(object).when(entry).getKey();
    doReturn(object).when(entry).getValue();
    doReturn(entries).when(populator).loadEntries();
    doNothing().when(cache).put(object, object);
    populator.pupulate();
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
    }
    verify(cache, atLeast(1)).put(object, object);
    verify(populator).loadEntries();
  }

}
