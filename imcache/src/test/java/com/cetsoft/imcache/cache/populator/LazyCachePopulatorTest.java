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
package com.cetsoft.imcache.cache.populator;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.pupulator.LazyCachePupulator;
	
/**
 * The Class LazyCachePopulatorTest.
 */
public class LazyCachePopulatorTest {

	/** The cache. */
	@Mock
	Cache<Object,Object> cache;
	
	/** The entry. */
	@Mock
	CacheEntry<Object, Object> entry;
	
	/** The populator. */
	LazyCachePupulator<Object, Object> populator;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		populator = spy(new LazyCachePupulator<Object, Object>(cache) {
			public List<CacheEntry<Object, Object>> loadEntries() {
				return null;
			}
		});
	}
	
	/**
	 * Populate.
	 */
	@Test
	public void populate(){
		Object object = new Object();
		List<CacheEntry<Object,Object>> entries = new ArrayList<CacheEntry<Object,Object>>();
		entries.add(entry);
		doReturn(object).when(entry).getKey();
		doReturn(object).when(entry).getValue();
		doReturn(entries).when(populator).loadEntries();
		doNothing().when(cache).put(object, object);
		populator.pupulate();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {}
		verify(cache,atLeast(1)).put(object, object);
		verify(populator).loadEntries();
	}
	
}
