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

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.offheap.OffHeapCache;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.search.index.IndexType;

public class VersionedOffHeapCacheBuilderTest {
	
	@Test
	public void build(){
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(8388608, 10);
		Cache<Object, Object> cache = CacheBuilder.offHeapCache()
		.storage(bufferStore)
		.cacheLoader(CacheBuilder.CACHE_LOADER)
		.evictionListener(CacheBuilder.EVICTION_LISTENER)
		.indexHandler(DummyIndexHandler.getInstance())
		.addIndex("age", IndexType.RANGE_INDEX)
		.serializer(CacheBuilder.SERIALIZER)
		.bufferCleanerPeriod(100)
		.bufferCleanerThreshold(0.6f)
		.concurrencyLevel(10)
		.evictionPeriod(100)
		.build();
		assert(cache instanceof SearchableCache);
		assert(cache instanceof OffHeapCache);
	}
}
