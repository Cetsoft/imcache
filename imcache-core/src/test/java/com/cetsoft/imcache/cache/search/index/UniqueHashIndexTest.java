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
