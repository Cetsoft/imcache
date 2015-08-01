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
* Date   : Sep 29, 2013
*/
package com.cetsoft.imcache.cache.search.index;

import java.util.List;

/**
 * The Interface CacheIndex is a data structure that improves the speed of 
 * data retrieval operations on a cache at the cost of additional writes and
 * the use of more storage space to maintain the extra copy of data. Indexes are
 * used to quickly locate data without having to search every object in a cache.
 */
public interface CacheIndex {

	/**
	 * Puts index on indexedKey.
	 *
	 * @param indexedKey the indexed key
	 * @param key the key
	 */
	void put(Object indexedKey, Object key);

	/**
	 * Removes the index from indexedKey.
	 *
	 * @param indexedKey the indexed key
	 * @param key the key
	 */
	void remove(Object indexedKey, Object key);

	/**
	 * Equals to.
	 *
	 * @param expectedValue the expected value
	 * @return the list
	 */
	List<Object> equalsTo(Object expectedValue);

	/**
	 * Less than.
	 *
	 * @param value the value
	 * @return the list
	 */
	List<Object> lessThan(Object value);

	/**
	 * Less than or equals to.
	 *
	 * @param value the value
	 * @return the list
	 */
	List<Object> lessThanOrEqualsTo(Object value);

	/**
	 * Greater than.
	 *
	 * @param value the value
	 * @return the list
	 */
	List<Object> greaterThan(Object value);

	/**
	 * Greater than or equals to.
	 *
	 * @param value the value
	 * @return the list
	 */
	List<Object> greaterThanOrEqualsTo(Object value);

	/**
	 * Between.
	 *
	 * @param lowerBound the lower bound
	 * @param upperBound the upper bound
	 * @return the list
	 */
	List<Object> between(Object lowerBound, Object upperBound);

}
