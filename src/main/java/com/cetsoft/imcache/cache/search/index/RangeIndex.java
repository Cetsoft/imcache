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
* Date   : Oct 26, 2013
*/
package com.cetsoft.imcache.cache.search.index;

import java.util.ArrayList;
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
public class RangeIndex extends MultiValueIndex{
	
	/**
	 * Instantiates a new range index.
	 */
	public RangeIndex(){
		this.map = new ConcurrentSkipListMap<Object, Set<Object>>();
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndexBase#lessThan(java.lang.Object)
	 */
	public List<Object> lessThan(Object value) {
		NavigableMap<Object, Set<Object>> map = getMap();
		Entry<Object,Set<Object>> current = map.floorEntry(value);
		while(current!=null&&current.getKey().equals(value)){
			current = map.floorEntry(current.getKey());
		}
		return floor(map, current);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndexBase#lessThanOrEqualsTo(java.lang.Object)
	 */
	public List<Object> lessThanOrEqualsTo(Object value) {
		NavigableMap<Object, Set<Object>> map = getMap();
		Entry<Object,Set<Object>> current = map.floorEntry(value);
		return floor(map, current);
	}

	/**
	 * Returns list of object which have associated key less than or equal to the current entry.
	 *
	 * @param map the map
	 * @param current the current
	 * @return the list of results
	 */
	protected List<Object> floor(NavigableMap<Object, Set<Object>> map, Entry<Object, Set<Object>> current) {
		Set<Object> resultSet = new HashSet<Object>();
		while(current!=null){
			synchronized (current) {
				resultSet.addAll(current.getValue());
				current = map.floorEntry(current.getKey());
			}
		}
		return new ArrayList<Object>(resultSet);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndexBase#greaterThan(java.lang.Object)
	 */
	public List<Object> greaterThan(Object value) {
		NavigableMap<Object, Set<Object>> map = getMap();
		Entry<Object,Set<Object>> current = map.higherEntry(value);
		while(current!=null&&current.getKey().equals(value)){
			current = map.higherEntry(current.getKey());
		}
		return higher(map, current);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndexBase#greaterThanOrEqualsTo(java.lang.Object)
	 */
	public List<Object> greaterThanOrEqualsTo(Object value) {
		NavigableMap<Object, Set<Object>> map = getMap();
		Entry<Object,Set<Object>> current = map.higherEntry(value);
		return floor(map, current);
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
		while(current!=null){
			synchronized (current) {
				resultSet.addAll(current.getValue());
				current = map.higherEntry(current.getKey());
			}
		}
		return new ArrayList<Object>(resultSet);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndexBase#between(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List<Object> between(Object lowerBound, Object higherBound) {
		Set<Object> resultSet = new HashSet<Object>();
		NavigableMap<Object, Set<Object>> map = getMap();
		Entry<Object,Set<Object>> current = map.higherEntry(lowerBound);
		while(current!=null&&((Comparable<Object>)current.getKey()).compareTo((Comparable<Object>)higherBound)<=0){
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
	public NavigableMap<Object, Set<Object>> getMap(){
		return (NavigableMap<Object, Set<Object>>) this.map;
	}
}
