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
* Date   : Aug 3, 2015
*/
package com.cetsoft.imcache.cache.builder;

import org.junit.Test;
import static org.junit.Assert.assertTrue;


public class CacheBuilderTest {
	
	@Test
	public void heapCache(){
		assertTrue(CacheBuilder.heapCache() instanceof HeapCacheBuilder);
	}
	
	@Test
	public void transactionalHeapCache(){
		assertTrue(CacheBuilder.transactionalHeapCache() instanceof TransactionalHeapCacheBuilder);
	}
	
	@Test
	public void concurrentHeapCache(){
		assertTrue(CacheBuilder.concurrentHeapCache() instanceof ConcurrentHeapCacheBuilder);
	}
	
	@Test
	public void offHeapCache(){
		assertTrue(CacheBuilder.offHeapCache() instanceof OffHeapCacheBuilder);
	}
	
	@Test
	public void versionedOffHeapCache(){
		assertTrue(CacheBuilder.versionedOffHeapCache() instanceof VersionedOffHeapCacheBuilder);
	}
	
}
