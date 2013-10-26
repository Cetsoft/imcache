/*
* Copyright (C) 2013 Cetsoft, http://www.cetsoft.com
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Library General Public
* License as published by the Free Software Foundation; either
* version 2 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Library General Public License for more details.
*
* You should have received a copy of the GNU Library General Public
* License along with this library; if not, write to the Free
* Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
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
public interface CacheIndex{
	
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
	 * @param higherBound the higher bound
	 * @return the list
	 */
	List<Object> between(Object lowerBound, Object higherBound);
	
}
