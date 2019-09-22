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
 * Date   : Aug 4, 2015
 */
package com.cetsoft.imcache.cache;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doReturn;

import com.cetsoft.imcache.cache.search.CacheQuery;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.filter.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AbstractSearchableCacheTest {

  @Mock
  Criteria criteria;

  @Mock
  Filter filter;

  @Mock
  IndexHandler<Integer, Item> indexHandler;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void execute() {
    AbstractSearchableCache<Integer, Item> abstractCache = new AbstractSearchableCache<Integer, Item>(
        "searchable-cache",
        key -> null, (key, value) -> {
    }, indexHandler) {
      final Map<Integer, Item> items = new HashMap<>();

      @Override
      public void put(Integer key, Item value) {
        items.put(key, value);
        indexHandler.add(key, value);
      }

      @Override
      public void put(Integer key, Item value, TimeUnit timeUnit, long duration) {
        items.put(key, value);
        indexHandler.add(key, value);
      }

      @Override
      public Item get(Integer key) {
        return null;
      }

      @Override
      public Item invalidate(Integer key) {
        final Item value = items.remove(key);
        indexHandler.remove(key, value);
        return null;
      }

      @Override
      public boolean contains(Integer key) {
        return false;
      }

      @Override
      public void clear() {

      }

      @Override
      public long size() {
        return 0;
      }

      @Override
      public CacheStats stats() {
        return null;
      }
    };
    Item item1 = new Item(1);
    Item item2 = new Item(2);
    abstractCache.put(1, item1);
    abstractCache.put(2, item1);
    List<Integer> keys = new ArrayList<Integer>();
    keys.add(1);
    keys.add(2);
    List<Item> values = new ArrayList<Item>();
    values.add(item1);
    values.add(item2);
    doReturn(keys).when(indexHandler).execute(any(Query.class));
    doReturn(values).when(filter).filter(anyList());
    List<Item> items = abstractCache
        .execute(CacheQuery.newQuery().setCriteria(criteria).setFilter(filter));
    assertTrue(items.contains(item1));
    assertTrue(items.contains(item2));
  }

  private static class Item {

    @SuppressWarnings("unused")
    private int value;

    public Item(int value) {
      this.value = value;
    }
  }

}
