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
* Date   : Jun 5, 2014
*/
package com.cetsoft.imcache.cache.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.cetsoft.imcache.cache.util.ThreadUtils;

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
				return ThreadUtils.createDaemonThread(runnable, "imcache:batchAsyncEvictionDrainer(thread="
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
