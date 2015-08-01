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
* Date   : Oct 26, 2013
*/
package com.cetsoft.imcache.cache.search.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The Class RangeIndex is type of index where tree indexed value 
 * can have one or more corresponding values.
 */
public class RangeIndex extends MultiValueIndex {

	/**
	 * Instantiates a new range index.
	 */
	public RangeIndex() {
		this.map = new ConcurrentSkipListMap<Object, Set<Object>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndexBase#lessThan(java.lang
	 * .Object)
	 */
	public List<Object> lessThan(Object value) {
		NavigableMap<Object, Set<Object>> map = getMap();
		return lower(map, map.lowerEntry(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndexBase#lessThanOrEqualsTo
	 * (java.lang.Object)
	 */
	public List<Object> lessThanOrEqualsTo(Object value) {
		List<Object> result = lessThan(value);
		equalsTo(value, result);
		return result;
	}

	/**
	 * Returns list of object which have associated key less than or equal to the current entry.
	 *
	 * @param map the map
	 * @param current the current
	 * @return the list of results
	 */
	protected List<Object> lower(NavigableMap<Object, Set<Object>> map, Entry<Object, Set<Object>> current) {
		Set<Object> resultSet = new HashSet<Object>();
		while (current != null) {
			synchronized (current) {
				resultSet.addAll(current.getValue());
				current = map.lowerEntry(current.getKey());
			}
		}
		return new ArrayList<Object>(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndexBase#greaterThan(java
	 * .lang.Object)
	 */
	public List<Object> greaterThan(Object value) {
		NavigableMap<Object, Set<Object>> map = getMap();
		Entry<Object, Set<Object>> current = map.higherEntry(value);
		return higher(map, current);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndexBase#greaterThanOrEqualsTo
	 * (java.lang.Object)
	 */
	public List<Object> greaterThanOrEqualsTo(Object value) {
		List<Object> result = greaterThan(value);
		equalsTo(value, result);
		return result;
	}

	/**
	 * Equals to the given value.
	 *
	 * @param value the value
	 * @param result the result
	 */
	protected void equalsTo(Object value, List<Object> result) {
		Collection<Object> results = map.get(value);
		if (results != null) {
			synchronized (results) {
				result.addAll(results);
			}
		}
	}

	/**
	 * Returns list of object which have associated key greater than or equal to the current entry..
	 *
	 * @param map the map
	 * @param current the current
	 * @return the list of results
	 */
	protected List<Object> higher(NavigableMap<Object, Set<Object>> map, Entry<Object, Set<Object>> current) {
		Set<Object> resultSet = new HashSet<Object>();
		while (current != null) {
			synchronized (current) {
				resultSet.addAll(current.getValue());
				current = map.higherEntry(current.getKey());
			}
		}
		return new ArrayList<Object>(resultSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndexBase#between(java.lang
	 * .Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List<Object> between(Object lowerBound, Object upperBound) {
		Set<Object> resultSet = new HashSet<Object>();
		NavigableMap<Object, Set<Object>> map = getMap();
		Entry<Object, Set<Object>> current = map.higherEntry(lowerBound);
		while (current != null
				&& ((Comparable<Object>) current.getKey()).compareTo((Comparable<Object>) upperBound) < 0) {
			synchronized (current) {
				resultSet.addAll(current.getValue());
				current = map.higherEntry(current.getKey());
			}
		}
		return new ArrayList<Object>(resultSet);
	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public NavigableMap<Object, Set<Object>> getMap() {
		return (NavigableMap<Object, Set<Object>>) this.map;
	}
}
