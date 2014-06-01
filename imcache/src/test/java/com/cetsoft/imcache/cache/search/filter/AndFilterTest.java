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

@SuppressWarnings("unchecked")
public class AndFilterTest {

	AndFilter andFilter;
	
	@Mock
	Filter filter;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		andFilter = spy(new AndFilter(filter, filter));
	}
	
	@Test
	public void filter(){
		Object object1 = new Object();
		Object object2 = new Object();
		Object object3 = new Object();
		List<Object> list1 = new ArrayList<Object>();
		list1.add(object1);
		list1.add(object2);
		List<Object> list2 = new ArrayList<Object>();
		list2.add(object2);
		list2.add(object3);
		doReturn(list1).doReturn(list2).when(filter).filter(anyList());
		List<Object> actualObjects = andFilter.filter(list1);
		assertEquals(object2, actualObjects.get(0));
	}

}
