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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.cetsoft.imcache.cache.util.ThreadUtils;

/**
 * The concurrent eviction listener interface for receiving eviction events. 
 * This class drains cache task queue and executes saveAll function for
 * cache tasks with multiple threads.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class ConcurrentEvictionListener<K, V> extends QueuingEvictionListener<K, V> {

	/** The Constant DEFAULT_CONCURRENCY_LEVEL. */
	public static final int DEFAULT_CONCURRENCY_LEVEL = 3;

	/** The Constant NO_OF_EVICTION_DRAINERS. */
	private static final AtomicInteger NO_OF_EVICTION_DRAINERS = new AtomicInteger();

	/**
	 * Instantiates a new concurrent eviction listener.
	 */
	public ConcurrentEvictionListener() {
		this(DEFAULT_BATCH_SIZE, DEFAULT_QUEUE_SIZE, DEFAULT_CONCURRENCY_LEVEL);
	}

	/**
	 * Instantiates a new concurrent eviction listener.
	 *
	 * @param batchSize the batch size
	 * @param queueSize the queue size
	 * @param concurrencyLevel the concurrency level
	 */
	public ConcurrentEvictionListener(int batchSize, int queueSize, int concurrencyLevel) {
		this.batchSize = batchSize;
		init(queueSize, concurrencyLevel);
	}

	/**
	 * Inits the.
	 *
	 * @param queueSize the queue size
	 * @param concurrencyLevel the concurrency level
	 */
	protected void init(int queueSize, int concurrencyLevel) {
		cacheTasks = new ArrayBlockingQueue<CacheTask<K, V>>(queueSize);
		ExecutorService drainerService = Executors.newFixedThreadPool(concurrencyLevel, new ThreadFactory() {
			public Thread newThread(Runnable runnable) {
				return ThreadUtils.createDaemonThread(runnable, "imcache:concurrentAsyncEvictionDrainer(thread="
						+ NO_OF_EVICTION_DRAINERS.incrementAndGet() + ")");
			}
		});
		// Creates runnables to drain cache task queue constantly.
		for (int i = 0; i < concurrencyLevel; i++) {
			drainerService.execute(new Runnable() {
				public void run() {
					while (true) {
						drainQueue();
					}
				}
			});
		}
	}

}
