/*
* Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.test;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferStore;

/**
 * The Class OffHeapCachePerformanceTest.
 */
public class OffHeapCachePerformanceTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String [] args) throws InterruptedException{
		final int NO_OF_THREADS = 100;
		final int SIZE = 1000;
		final AtomicLong totalTimeForOffHeap = new AtomicLong();
		final AtomicLong totalTimeForMap = new AtomicLong();
		final int NO_OF_ITERATIONS = 1000000;
		final CountDownLatch offHeapLatch = new CountDownLatch(NO_OF_THREADS);
		final CountDownLatch mapLatch = new CountDownLatch(NO_OF_THREADS);
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(100000, 10);
		final Cache<Integer,SimpleObject> offHeapCache = CacheBuilder.offHeapCache().storage(bufferStore).build();
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
	
	/**
	 * The Class SimpleObject.
	 */
	@SuppressWarnings("unused")
	private static class SimpleObject implements Serializable{
		
		private static final long serialVersionUID = 196026337851914850L;
		/** The y. */
		protected int x =3,y=4; 
		/** The str. */
		protected String str = "";
	}
}
