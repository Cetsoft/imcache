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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.offheap.VersionedOffHeapCache;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;

public class VersionedOffHeapCacheBuilderTest {
	
	@Test
	public void build(){
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(8388608, 10);
		Cache<Object, Object> cache = CacheBuilder.versionedOffHeapCache()
		.storage(bufferStore)
		.cacheLoader(AbstractCacheBuilder.CACHE_LOADER)
		.evictionListener(AbstractCacheBuilder.EVICTION_LISTENER)
		.indexHandler(DummyIndexHandler.getInstance())
		.addIndex("age", IndexType.RANGE_INDEX)
		.serializer(AbstractCacheBuilder.SERIALIZER)
		.bufferCleanerPeriod(100)
		.bufferCleanerThreshold(0.6f)
		.concurrencyLevel(10)
		.evictionPeriod(100)
		.build();
		assertTrue(cache instanceof SearchableCache);
		assertTrue(cache instanceof VersionedOffHeapCache);
	}
	
	@Test(expected=NecessaryArgumentException.class)
	public void buildThrowsNecessaryArgumentException(){
		CacheBuilder.transactionalHeapCache().build();
	}
}
