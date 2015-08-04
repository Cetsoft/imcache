package com.cetsoft.imcache.spring;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cetsoft.imcache.cache.heap.ConcurrentHeapCache;
import com.cetsoft.imcache.cache.heap.HeapCache;
import com.cetsoft.imcache.cache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.offheap.OffHeapCache;
import com.cetsoft.imcache.cache.offheap.VersionedOffHeapCache;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferStore;

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
