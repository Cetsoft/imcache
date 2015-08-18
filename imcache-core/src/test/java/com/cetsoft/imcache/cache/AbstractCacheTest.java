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
package com.cetsoft.imcache.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.heap.HeapCache;

public class AbstractCacheTest {
	
	@Mock
	IndexHandler<Integer, Item> indexHandler;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void hitRatio(){
		AbstractCache<Integer, Item> abstractCache = new HeapCache<Integer, Item>(new CacheLoader<Integer, Item>() {
			public Item load(Integer key) {return null;}
		}, new EvictionListener<Integer, Item>() {
			public void onEviction(Integer key, Item value) {}
		}, indexHandler, 10);
		long hit = 2;
		long miss = 3;
		assertEquals(2/5.0, abstractCache.hitRatio(hit, miss), 0.000001);
	}
	
	@Test
	public void hitRatioWithZeroMissAndHit(){
		AbstractCache<Integer, Item> abstractCache = new HeapCache<Integer, Item>(new CacheLoader<Integer, Item>() {
			public Item load(Integer key) {return null;}
		}, new EvictionListener<Integer, Item>() {
			public void onEviction(Integer key, Item value) {}
		}, indexHandler, 10);
		assertEquals(0.0, abstractCache.hitRatio(0, 0), 0.000001);
	}
	
	private static class Item{}
	
}
