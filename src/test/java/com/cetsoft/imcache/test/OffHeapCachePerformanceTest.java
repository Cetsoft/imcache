package com.cetsoft.imcache.test;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.serialization.Serializer;

public class OffHeapCachePerformanceTest {

	public static void main(String [] args) throws InterruptedException{
		final int NO_OF_THREADS = 100;
		final int SIZE = 1000;
		final AtomicLong totalTimeForOffHeap = new AtomicLong();
		final AtomicLong totalTimeForMap = new AtomicLong();
		final int NO_OF_ITERATIONS = 1000000;
		final CountDownLatch offHeapLatch = new CountDownLatch(NO_OF_THREADS);
		final CountDownLatch mapLatch = new CountDownLatch(NO_OF_THREADS);
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(100000, 10);
		final Cache<Integer,SimpleObject> offHeapCache = CacheBuilder.offHeapCache().serializer(new Serializer<SimpleObject>() {
			public byte[] serialize(SimpleObject value) {
				return com.cetsoft.imcache.test.Serializer.serialize(value);
			}
			public SimpleObject deserialize(byte[] payload) {
				return com.cetsoft.imcache.test.Serializer.deserialize(payload);
			}
		}).storage(bufferStore).build();
		for (int i = 0; i < NO_OF_THREADS; i++) {
			new Thread(new Runnable() {
				public void run() {
					long start = System.currentTimeMillis();
					for (int j = 0; j < NO_OF_ITERATIONS; j++) {
						offHeapCache.put((int) (Math.random()*SIZE), new SimpleObject());
					}
					long stop = System.currentTimeMillis();
					totalTimeForOffHeap.addAndGet(stop-start);
					offHeapLatch.countDown();
				}
			}).start();
		}
		final Map<Integer,SimpleObject> concurrentMap = new ConcurrentHashMap<Integer,SimpleObject>();
		for (int i = 0; i < NO_OF_THREADS; i++) {
			new Thread(new Runnable() {
				public void run() {
					long start = System.currentTimeMillis();
					for (int j = 0; j < NO_OF_ITERATIONS; j++) {
						concurrentMap.put((int) (Math.random()*SIZE), new SimpleObject());
					}
					long stop = System.currentTimeMillis();
					totalTimeForMap.addAndGet(stop-start);
					mapLatch.countDown();
				}
			}).start();
		}
		offHeapLatch.await();
		mapLatch.await();
		System.out.println("OffHeap = "+totalTimeForOffHeap.get());
		System.out.println("Map = "+totalTimeForMap.get());
	}
	
	private static class SimpleObject implements Serializable{
		protected int x =3,y=4; String str = "";
	}
}
