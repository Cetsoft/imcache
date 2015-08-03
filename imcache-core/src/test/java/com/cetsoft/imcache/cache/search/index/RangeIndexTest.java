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
* Date   : May 25, 2014
*/
package com.cetsoft.imcache.cache.search.index;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * The Class RangeIndexTest.
 */
public class RangeIndexTest {

	/** The range index. */
	RangeIndex rangeIndex;
	
	/** The upper bound. */
	@Mock
	Comparable<Object> comparable, lowerBound, upperBound;
	
	/** The map. */
	@Mock
	NavigableMap<Object, Set<Object>> map;
	
	/** The entry. */
	@Mock
	Entry<Object, Set<Object>> entry;
	
	/** The result. */
	@Spy
	List<Object> result = new ArrayList<Object>();
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		rangeIndex = spy(new RangeIndex());
		rangeIndex.map = map;
	}
	
	/**
	 * Less than.
	 */
	@Test
	public void lessThan(){
		Object value = new Object();
		doReturn(entry).when(map).lowerEntry(value);
		doReturn(result).when(rangeIndex).lower(map, entry);
		rangeIndex.lessThan(value);
		verify(rangeIndex).lower(map, entry);
	}
	
	/**
	 * Less than or equals to.
	 */
	@Test
	public void lessThanOrEqualsTo(){
		Object value = new Object();
		doReturn(entry).when(map).lowerEntry(value);
		doReturn(result).when(rangeIndex).lessThan(value);
		doNothing().when(rangeIndex).equalsTo(value, result);
		rangeIndex.lessThanOrEqualsTo(value);
		verify(rangeIndex).lessThan(value);
		verify(rangeIndex).equalsTo(value, result);
	}
	
	/**
	 * Lower.
	 */
	@Test
	public void lower(){
		Object key = new Object();
		Object value = new Object();
		Set<Object> set = new HashSet<Object>();
		set.add(value);
		doReturn(set).when(entry).getValue();
		doReturn(key).when(entry).getKey();
		doReturn(null).when(map).lowerEntry(key);
		List<Object> actualObjects = rangeIndex.lower(map, entry);
		assertEquals(value, actualObjects.get(0));
	}
	
	/**
	 * Greater than.
	 */
	@Test
	public void greaterThan(){
		Object value = new Object();
		doReturn(entry).when(map).higherEntry(value);
		doReturn(result).when(rangeIndex).higher(map, entry);
		rangeIndex.greaterThan(value);
		verify(rangeIndex).higher(map, entry);
	}
	
	/**
	 * Greater than or equals to.
	 */
	@Test
	public void greaterThanOrEqualsTo(){
		Object value = new Object();
		doReturn(entry).when(map).higherEntry(value);
		doReturn(result).when(rangeIndex).greaterThan(value);
		doNothing().when(rangeIndex).equalsTo(value, result);
		rangeIndex.greaterThanOrEqualsTo(value);
		verify(rangeIndex).greaterThan(value);
		verify(rangeIndex).equalsTo(value, result);
	}
	
	/**
	 * Higher.
	 */
	@Test
	public void higher(){
		Object key = new Object();
		Object value = new Object();
		Set<Object> set = new HashSet<Object>();
		set.add(value);
		doReturn(set).when(entry).getValue();
		doReturn(key).when(entry).getKey();
		doReturn(null).when(map).higherEntry(key);
		List<Object> actualObjects = rangeIndex.higher(map, entry);
		assertEquals(value, actualObjects.get(0));
	}
	
	/**
	 * Equals to.
	 */
	@Test
	public void equalsTo(){
		List<Object> list = new ArrayList<Object>();
		Object value = new Object();
		Set<Object> set = new HashSet<Object>();
		set.add(value);
		doReturn(set).when(map).get(value);
		rangeIndex.equalsTo(value, list);
		assertTrue(list.contains(value));
	}
	
	/**
	 * Between.
	 */
	@Test
	public void between(){
		Comparable<Object> key = comparable;
		Object value = new Object();
		Set<Object> set = new HashSet<Object>();
		set.add(value);
		doReturn(set).when(entry).getValue();
		doReturn(key).when(entry).getKey();
		doReturn(entry).when(map).higherEntry(lowerBound);
		doReturn(-1).doReturn(1).when(key).compareTo(upperBound);
		doReturn(null).when(map).higherEntry(key);
		List<Object> list = rangeIndex.between(lowerBound, upperBound);
		assertTrue(list.contains(value));
	}
	
}
