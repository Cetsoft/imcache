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

import com.cetsoft.imcache.cache.search.criteria.AndCriteria;
import com.cetsoft.imcache.cache.search.criteria.ArithmeticCriteria;
import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.criteria.DiffCriteria;
import com.cetsoft.imcache.cache.search.criteria.OrCriteria;
import com.cetsoft.imcache.cache.search.index.CacheIndex;
import com.cetsoft.imcache.cache.search.index.IndexNotFoundException;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.cache.search.index.NonUniqueHashIndex;
import com.cetsoft.imcache.cache.search.index.RangeIndex;
import com.cetsoft.imcache.cache.search.index.UniqueHashIndex;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Class DefaultIndexHandler implements basic query execution.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class DefaultIndexHandler<K, V> implements IndexHandler<K, V> {

  /**
   * The indexes.
   */
  protected Map<String, CacheIndex> indexes;

  /**
   * Instantiates a new simple query executor.
   */
  public DefaultIndexHandler() {
    indexes = new HashMap<>();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.cache.search.Indexable#addIndex(com.cetsoft.imcache
   * .cache.search.CacheIndex)
   */
  public void addIndex(final String attributeName, final IndexType type) {
    if (type == IndexType.UNIQUE_HASH) {
      indexes.put(attributeName, new UniqueHashIndex());
    } else if (type == IndexType.NON_UNIQUE_HASH) {
      indexes.put(attributeName, new NonUniqueHashIndex());
    } else if (type == IndexType.RANGE_INDEX) {
      indexes.put(attributeName, new RangeIndex());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.cache.search.IndexHandler#add(java.lang.Object,
   * java.lang.Object)
   */
  public void add(final K key, final V value) {
    for (final String attributeName : indexes.keySet()) {
      final Object indexedKey = getIndexedKey(attributeName, value);
      if (indexedKey == null) {
        throw new NullPointerException();
      }
      indexes.get(attributeName).put(indexedKey, key);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.cache.search.IndexHandler#remove(java.lang.Object,
   * java.lang.Object)
   */
  public void remove(final K key, final V value) {
    for (final String attributeName : indexes.keySet()) {
      final Object indexedKey = getIndexedKey(attributeName, value);
      if (indexedKey == null) {
        throw new NullPointerException();
      }
      indexes.get(attributeName).remove(indexedKey, key);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.cache.search.IndexHandler#clear()
   */
  public void clear() {
    indexes.clear();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.cache.search.IndexHandler#execute(com.cetsoft.imcache
   * .cache.search.Query)
   */
  @SuppressWarnings("unchecked")
  public List<K> execute(final Query query) {
    List<Object> results = execute(query.getCriteria());
    return (List<K>) results;
  }

  /**
   * Execute.
   *
   * @param criteria the criteria
   * @return the list
   */
  protected List<Object> execute(final Criteria criteria) {
    if (criteria instanceof ArithmeticCriteria) {
      return executeArithmetic((ArithmeticCriteria) criteria);
    } else if (criteria instanceof AndCriteria) {
      return executeAnd((AndCriteria) criteria);
    } else if (criteria instanceof OrCriteria) {
      return executeOr((OrCriteria) criteria);
    } else {
      return executeDiff((DiffCriteria) criteria);
    }
  }

  /**
   * Execute arithmetic.
   *
   * @param arithmeticCriteria the criteria
   * @return the list
   */
  protected List<Object> executeArithmetic(final ArithmeticCriteria arithmeticCriteria) {
    final CacheIndex cacheIndex = indexes.get(arithmeticCriteria.getAttributeName());
    if (cacheIndex == null) {
      throw new IndexNotFoundException();
    }
    return arithmeticCriteria.meets(cacheIndex);
  }

  /**
   * Execute and.
   *
   * @param andCriteria the criteria
   * @return the list
   */
  protected List<Object> executeAnd(final AndCriteria andCriteria) {
    List<Object> results = new ArrayList<Object>();
    for (final Criteria innerCriteria : andCriteria.getCriterias()) {
      List<Object> result = execute(innerCriteria);
      results = getObjects(results, result);
    }
    return results;
  }

  private List<Object> getObjects(List<Object> results, List<Object> result) {
    if (results.size() == 0) {
      results.addAll(result);
    } else {
      final List<Object> mergedResults = new ArrayList<>(results.size());
      for (final Object object : result) {
        if (results.contains(object)) {
          mergedResults.add(object);
        }
      }
      results = mergedResults;
    }
    return results;
  }

  /**
   * Execute diff.
   *
   * @param diffCriteria the criteria
   * @return the list
   */
  protected List<Object> executeDiff(final DiffCriteria diffCriteria) {
    final List<Object> leftResult = execute(diffCriteria.getLeftCriteria());
    final List<Object> rightResult = execute(diffCriteria.getRightCriteria());
    for (final Object object : rightResult) {
      leftResult.remove(object);
    }
    return leftResult;
  }

  /**
   * Execute or.
   *
   * @param orCriteria the criteria
   * @return the list
   */
  protected List<Object> executeOr(final OrCriteria orCriteria) {
    final Set<Object> results = new HashSet<Object>();
    for (Criteria innerCriteria : orCriteria.getCriterias()) {
      final List<Object> result = execute(innerCriteria);
      results.addAll(result);
    }
    return new ArrayList<>(results);
  }

  /**
   * Gets the indexed key.
   *
   * @param attributeName the attribute name
   * @param value the value
   * @return the indexed key
   */
  protected Object getIndexedKey(String attributeName, V value) {
    try {
      Field field = value.getClass().getDeclaredField(attributeName);
      field.setAccessible(true);
      return field.get(value);
    } catch (Exception e) {
      throw new AttributeException(e);
    }
  }

}
