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
* Date   : Sep 28, 2013
*/
package com.cetsoft.imcache.cache.search;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

/**
 * The Class DefaultIndexHandler implements basic query execution.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class DefaultIndexHandler<K, V> implements IndexHandler<K, V> {

	/** The indexes. */
	protected Map<String, CacheIndex> indexes;

	/**
	 * Instantiates a new simple query executer.
	 */
	public DefaultIndexHandler() {
		indexes = new HashMap<String, CacheIndex>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.Indexable#addIndex(com.cetsoft.imcache
	 * .cache.search.CacheIndex)
	 */
	public void addIndex(String attributeName, IndexType type) {
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
	public void add(K key, V value) {
		for (String attributeName : indexes.keySet()) {
			Object indexedKey = getIndexedKey(attributeName, value);
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
	public void remove(K key, V value) {
		for (String attributeName : indexes.keySet()) {
			Object indexedKey = getIndexedKey(attributeName, value);
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
	public List<K> execute(Query query) {
		List<Object> results = execute(query.getCriteria());
		return (List<K>) results;
	}

	/**
	 * Execute.
	 *
	 * @param criteria the criteria
	 * @return the list
	 */
	protected List<Object> execute(Criteria criteria) {
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
	 * @param criteria the criteria
	 * @return the list
	 */
	protected List<Object> executeArithmetic(ArithmeticCriteria arithmeticCriteria) {
		CacheIndex cacheIndex = indexes.get(arithmeticCriteria.getAttributeName());
		if (cacheIndex == null) {
			throw new IndexNotFoundException();
		}
		return arithmeticCriteria.meets(cacheIndex);
	}

	/**
	 * Execute and.
	 *
	 * @param criteria the criteria
	 * @return the list
	 */
	protected List<Object> executeAnd(AndCriteria andCriteria) {
		List<Object> results = new ArrayList<Object>();
		for (Criteria innerCriteria : andCriteria.getCriterias()) {
			List<Object> result = execute(innerCriteria);
			if (results.size() == 0) {
				results.addAll(result);
			} else {
				List<Object> mergedResults = new ArrayList<Object>(results.size());
				for (Object object : result) {
					if (results.contains(object)) {
						mergedResults.add(object);
					}
				}
				results = mergedResults;
			}
		}
		return results;
	}

	/**
	 * Execute diff.
	 *
	 * @param criteria the criteria
	 * @return the list
	 */
	protected List<Object> executeDiff(DiffCriteria diffCriteria) {
		List<Object> leftResult = execute(diffCriteria.getLeftCriteria());
		List<Object> rightResult = execute(diffCriteria.getRightCriteria());
		for (Object object : rightResult) {
			leftResult.remove(object);
		}
		return leftResult;
	}

	/**
	 * Execute or.
	 *
	 * @param criteria the criteria
	 * @return the list
	 */
	protected List<Object> executeOr(OrCriteria orCriteria) {
		Set<Object> results = new HashSet<Object>();
		for (Criteria innerCriteria : orCriteria.getCriterias()) {
			List<Object> result = execute(innerCriteria);
			results.addAll(result);
		}
		return new ArrayList<Object>(results);
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
