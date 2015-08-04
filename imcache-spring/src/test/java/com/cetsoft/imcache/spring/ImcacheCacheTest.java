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
* Date   : Aug 4, 2015
*/
package com.cetsoft.imcache.spring;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.support.SimpleValueWrapper;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;

public class ImcacheCacheTest {
	
	Cache<Object, Object> cache;
	ImcacheCache imcache;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		cache = spy(CacheBuilder.concurrentHeapCache().build());
		imcache = new ImcacheCache(cache);
	}
	
	@Test
	public void getName(){
		assertEquals(cache.getName(), imcache.getName());
	}
	
	@Test
	public void getNativeCache(){
		assertEquals(cache, imcache.getNativeCache());
	}
	
	@Test
	public void get(){
		cache.put(1, 1);
		assertEquals(new SimpleValueWrapper(1).get(), imcache.get(1).get());
	}
	
	@Test
	public void getKeyNull(){
		assertEquals(null, imcache.get(null));
	}
	
	@Test
	public void getWithClass(){
		cache.put(1, 1);
		assertTrue(1 == imcache.get(1, Integer.class));
	}
	
	@Test
	public void getWithClassKeyNull(){
		assertTrue(null == imcache.get(null, Integer.class));
	}
	
	@Test
	public void put(){
		imcache.put(1, 1);
		assertEquals(1, cache.get(1));
		verify(cache).put(1, 1);
	}
	
	@Test
	public void clear(){
		imcache.clear();
		verify(cache).clear();;
	}
}
