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
package com.cetsoft.imcache.cache.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import static org.junit.Assert.*;

import com.cetsoft.imcache.concurrent.ConcurrentLinkedHashMap;
import com.cetsoft.imcache.concurrent.EvictionListener;
import com.cetsoft.imcache.concurrent.Weigher;
import com.cetsoft.imcache.concurrent.Weighers;

public class ConcurrentLinkedHashMapTest {
	
	@Test
	public void concurrentLinkedHashMap(){
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		int concurrencyLevel = 2;
		int initialCapacity = 2;
		int capacity = 2;
		long period = 3L;
		EvictionListener<Integer, Integer> evictionListener = new EvictionListener<Integer, Integer>() {
			@Override
			public void onEviction(Integer key, Integer value) {}
		};
		Weigher<Integer> weigher = Weighers.singleton();
		ConcurrentLinkedHashMap<Integer, Integer> map = new ConcurrentLinkedHashMap<Integer, Integer>(
				concurrencyLevel, capacity, initialCapacity, weigher , period, service, 
				evictionListener);
		map.put(1, 2);
		map.ascendingKeySet();
		map.ascendingKeySetWithLimit(10);
		map.ascendingMap();
		map.ascendingMapWithLimit(10);
		assertEquals(capacity, map.capacity());
		assertFalse(map.containsKey(3));
		map.descendingKeySet();
		map.descendingKeySetWithLimit(10);
		map.descendingMap();
		map.descendingMapWithLimit(10);
		map.entrySet();
		assertTrue(map.keySet().contains(1));
		map.putAll(map);
		map.putIfAbsent(3, 4);
		map.remove(3);
		map.replace(1, 10);
		map.replace(1, 10, 100);
		map.setCapacity(20);
		map.size();
		map.isEmpty();
		map.weightedSize();
	}
}
