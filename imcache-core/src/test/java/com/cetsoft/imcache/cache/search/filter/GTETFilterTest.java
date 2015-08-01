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
* Date   : Jun 1, 2014
*/
package com.cetsoft.imcache.cache.search.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * The Class GTETFilterTest.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GTETFilterTest {

	/** The gtet filter. */
	GTETFilter gtetFilter;
	
	/** The comparable. */
	@Mock
	Comparable comparable;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		gtetFilter = spy(new GTETFilter("x", new Comparable() {
			public int compareTo(Object o) {return 0;}
		}));
	}
	
	/**
	 * Filter.
	 */
	@Test
	public void filter(){
		List<Object> objects= new ArrayList<Object>();
		objects.add(comparable);
		objects.add(comparable);
		doReturn(0).doReturn(1).when(comparable).compareTo(any());
		doReturn(comparable).when(gtetFilter).getAttributeValue(comparable);
		List<Object> actualObjects = gtetFilter.filter(objects);
		assertEquals(comparable, actualObjects.get(0));
		assertEquals(comparable, actualObjects.get(1));
	}

}
