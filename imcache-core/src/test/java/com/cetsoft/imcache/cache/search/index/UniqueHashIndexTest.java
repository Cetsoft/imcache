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
 * Date   : May 25, 2014
 */
package com.cetsoft.imcache.cache.search.index;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.search.index.UniqueHashIndex;

public class UniqueHashIndexTest {

	/** The index base. */
	UniqueHashIndex index;
	
	@Mock
	Map<Object, Object> map;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		index = spy(new UniqueHashIndex());
		index.map = map;
	}
	
	@Test
	public void put(){
		Object object = new Object();
		doReturn(null).when(map).put(object, object);
		index.put(object, object);
		verify(map).put(object, object);
	}
	
	@Test
	public void remove(){
		Object object = new Object();
		doReturn(true).when(map).remove(object);
		index.remove(object, object);
		verify(map).remove(object);
	}
	
	@Test
	public void equalsTo(){
		Object object = new Object();
		doReturn(object).when(map).get(object);
		List<Object> actualObjects = index.equalsTo(object);
		assertEquals(object, actualObjects.get(0));
	}
	
}
