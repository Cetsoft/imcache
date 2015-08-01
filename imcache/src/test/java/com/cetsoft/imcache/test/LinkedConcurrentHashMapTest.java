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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.concurrent.ConcurrentLinkedHashMap;
import com.cetsoft.imcache.concurrent.EvictionListener;
import com.cetsoft.imcache.concurrent.Weigher;
import com.cetsoft.imcache.concurrent.Weighers;

/**
 * The Class LinkedConcurrentHashMapTest.
 */
public class LinkedConcurrentHashMapTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {
		int noOfThreads = 30;
		final int size = 1000;
		final int multiplier = 10;
		final int noOfGets = 100;
		final int noOfIterations = 1000;
		Weigher<Integer> weigher = Weighers.singleton();
		final Map<Integer, Integer> myMap = new ConcurrentLinkedHashMap<Integer, Integer>(16, 100, size, 
				weigher, 2000,
				Executors.newScheduledThreadPool(1, new ThreadFactory() {
					public Thread newThread(Runnable runnable) {
						return new Thread("imcache:evictionThread");
					}
				}),
				new EvictionListener<Integer, Integer>() {
					public void onEviction(Integer key, Integer value) {
						
					}
				});
		final Map<Integer, Integer> conMap = new ConcurrentHashMap<Integer, Integer>(size);
		final AtomicLong myAtomicLong = new AtomicLong();
		final AtomicLong conAtomicLong = new AtomicLong();
		final CountDownLatch myCountDownLatch = new CountDownLatch(noOfThreads);
		final CountDownLatch conCountDownLatch = new CountDownLatch(noOfThreads);
		for (int i = 0; i < noOfThreads; i++) {
			new Thread(new Runnable() {
				public void run() {
					for (int i = 0; i < noOfIterations; i++) {
						long start = System.currentTimeMillis();
						myMap.put((int) (Math.random() * size * multiplier), 0);
						for (int j = 0; j < noOfGets; j++) {
							myMap.get((int) (Math.random() * size));
						}
						long stop = System.currentTimeMillis();
						myAtomicLong.addAndGet(stop - start);
					}
					myCountDownLatch.countDown();
				}
			}).start();
		}
		for (int i = 0; i < noOfThreads; i++) {
			new Thread(new Runnable() {
				public void run() {
					for (int i = 0; i < noOfIterations; i++) {
						long start = System.currentTimeMillis();
						conMap.put((int) (Math.random() * size * multiplier), 0);
						for (int j = 0; j < noOfGets; j++) {
							conMap.get((int) (Math.random() * size));
						}
						long stop = System.currentTimeMillis();
						conAtomicLong.addAndGet(stop - start);
					}
					conCountDownLatch.countDown();
				}
			}).start();
		}
		myCountDownLatch.await();
		conCountDownLatch.await();
		System.out.println(myAtomicLong.get());
		System.out.println(conAtomicLong.get());
	}

}
