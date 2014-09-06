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
 * Date   : Jun 5, 2014
 */
package com.cetsoft.imcache.cache.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The scheduled eviction listener interface for receiving eviction events. 
 * This class drains cache task queue and executes saveAll function in a
 * scheduled manner.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class ScheduledEvictionListener<K, V> extends QueuingEvictionListener<K, V> {

	/** The Constant DEFAULT_PERIOD. */
	public static final long DEFAULT_PERIOD = 3000;

	/** The Constant NO_OF_EVICTION_DRAINERS. */
	private static final AtomicInteger NO_OF_EVICTION_DRAINERS = new AtomicInteger();

	/**
	 * Instantiates a new scheduled eviction listener.
	 */
	public ScheduledEvictionListener() {
		this(DEFAULT_BATCH_SIZE, DEFAULT_PERIOD, DEFAULT_QUEUE_SIZE);
	}

	/**
	 * Instantiates a new scheduled eviction listener.
	 *
	 * @param batchSize the batch size
	 * @param period the period
	 * @param queueSize the queue size
	 */
	public ScheduledEvictionListener(int batchSize, long period, int queueSize) {
		this.batchSize = batchSize;
		init(period, queueSize);
	}

	/**
	 * Inits the.
	 *
	 * @param period the period
	 * @param queueSize the queue size
	 */
	protected void init(long period, int queueSize) {
		cacheTasks = new ArrayBlockingQueue<CacheTask<K, V>>(queueSize);
		ScheduledExecutorService drainerService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			public Thread newThread(Runnable runnable) {
				return new Thread(runnable, "imcache:batchAsyncEvictionDrainer(thread="
						+ NO_OF_EVICTION_DRAINERS.incrementAndGet() + ")");
			}
		});
		drainerService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				drainQueue();
			}
		}, period, period, TimeUnit.MILLISECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.async.QueuingEvictionListener#drainQueue()
	 */
	@Override
	protected void drainQueue() {
		while (!cacheTasks.isEmpty()) {
			super.drainQueue();
		}
	}

}
