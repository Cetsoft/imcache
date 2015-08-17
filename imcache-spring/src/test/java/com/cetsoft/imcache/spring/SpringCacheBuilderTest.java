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

import org.junit.Test;

import com.cetsoft.imcache.heap.ConcurrentHeapCache;
import com.cetsoft.imcache.heap.HeapCache;
import com.cetsoft.imcache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.offheap.OffHeapCache;
import com.cetsoft.imcache.offheap.VersionedOffHeapCache;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;

public class SpringCacheBuilderTest {
	
	@Test
	public void build(){
		SpringCacheBuilder builder = new SpringCacheBuilder();
		
		builder.setType("heap");
		assertTrue(builder.build() instanceof HeapCache);
		
		builder.setTransactionCommitter(new TransactionCommitter<Object, Object>() {
			@Override
			public void doPut(Object key, Object value) {}
		});
		builder.setType("transactionalheap");
		assertTrue(builder.build()  instanceof TransactionalHeapCache);
		
		builder.setType("concurrentheap");
		builder.setConcurrencyLevel(2);
		assertTrue(builder.build()  instanceof ConcurrentHeapCache);
		
		builder.setType("offheap");
		builder.setEvictionPeriod(2);
		builder.setBufferCleanerPeriod(1000);
		builder.setBufferCleanerThreshold(0.6F);
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(8388608, 2);
		builder.setBufferStore(bufferStore);
		assertTrue(builder.build()  instanceof OffHeapCache);
		
		builder.setType("versionedoffheap");
		assertTrue(builder.build() instanceof VersionedOffHeapCache);
	}
}
