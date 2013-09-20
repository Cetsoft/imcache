package com.cetsoft.imcache.heap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.concurrent.ConcurrentLinkedHashMap;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		int noOfThreads = 30;
		final int size = 10000;
		final int multiplier = 10;
		final int noOfGets = 100;
		final int noOfIterations = 100000;
		final Map<Integer, Integer> myMap = new ConcurrentLinkedHashMap<Integer, Integer>(size);
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
