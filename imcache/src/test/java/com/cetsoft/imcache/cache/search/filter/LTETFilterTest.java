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

@SuppressWarnings({"rawtypes", "unchecked"})
public class LTETFilterTest {

	LTETFilter ltetFilter;
	
	@Mock
	Comparable comparable;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		ltetFilter = spy(new LTETFilter("x", new Comparable() {
			public int compareTo(Object o) {return 0;}
		}));
	}
	
	@Test
	public void filter(){
		List<Object> objects= new ArrayList<Object>();
		objects.add(comparable);
		objects.add(comparable);
		doReturn(0).doReturn(-1).when(comparable).compareTo(any());
		doReturn(comparable).when(ltetFilter).getAttributeValue(comparable);
		List<Object> actualObjects = ltetFilter.filter(objects);
		assertEquals(comparable, actualObjects.get(0));
		assertEquals(comparable, actualObjects.get(1));
	}

}
