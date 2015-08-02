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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class NonUniqueHashIndexTest {

	/** The index. */
	MultiValueIndex index;
	
	@Spy
	Map<Object, Set<Object>> map = new HashMap<Object, Set<Object>>();
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		index = spy(new NonUniqueHashIndex());
		index.map = map;
	}
	
	@Test
	public void put(){
		Object object = new Object();
		index.put(object, object);
		assertTrue(index.map.get(object).contains(object));
	}
	
	@Test
	public void remove(){
		Object object = new Object();
		index.remove(object, object);
		verify(map, times(0)).remove(object);
	}
	
	@Test
	public void removeFromBothMapAndSet(){
		Object object = new Object();
		Set<Object> set = spy(new HashSet<Object>());
		map.put(object, set);
		map.get(object).add(object);
		index.remove(object, object);
		verify(set).remove(object);
		verify(map).remove(object);
	}
	
	@Test
	public void equalsToEmptyResult(){
		Object object = new Object();
		List<Object> result = index.equalsTo(object);
		assertTrue(result.size()==0);
	}
	
	@Test
	public void equalsTo(){
		Object object = new Object();
		Set<Object> set = spy(new HashSet<Object>());
		map.put(object, set);
		set.add(object);
		List<Object> result = index.equalsTo(object);
		assertEquals(object, result.get(0));
	}
	
}
