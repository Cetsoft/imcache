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
