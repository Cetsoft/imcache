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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * The queuing eviction listener interface for receiving eviction events. 
 * When eviction occurs, this class passes key, value pair to cache task, 
 * which is later executed by the listener at some time in the future based
 * on the implementation.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class QueuingEvictionListener<K, V> implements AsyncEvictionListener<K, V> {

	/** The Constant DEFAULT_BATCH_SIZE. */
	public static final int DEFAULT_BATCH_SIZE = 1000;

	/** The Constant DEFAULT_QUEUE_SIZE. */
	public static final int DEFAULT_QUEUE_SIZE = 10000;

	/** The cache tasks. */
	protected BlockingQueue<CacheTask<K, V>> cacheTasks;

	/** The batch size. */
	protected int batchSize;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.EvictionListener#onEviction(java.lang.Object,
	 * java.lang.Object)
	 */
	public void onEviction(K key, V value) {
		while (true) {
			try {
				CacheTask<K, V> cacheTask = createCacheTask(key, value);
				cacheTasks.put(cacheTask);
				return;
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Creates the cache task.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the cache task
	 */
	public CacheTask<K, V> createCacheTask(K key, V value) {
		return new SimpleCacheTask<K, V>(key, value);
	}

	/**
	 * Drain queue.
	 */
	protected void drainQueue() {
		List<CacheTask<K, V>> cacheTasksToBeDrained = new ArrayList<CacheTask<K, V>>(batchSize);
		cacheTasks.drainTo(cacheTasksToBeDrained, batchSize);
		save(cacheTasksToBeDrained);
	}

	/**
	 * Save all.
	 *
	 * @param cacheTasks the cache tasks
	 */
	public abstract void save(List<CacheTask<K, V>> cacheTasks);

	/**
	 * The Class SimpleCacheTask.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	protected static class SimpleCacheTask<K, V> implements CacheTask<K, V> {

		/** The key. */
		private K key;

		/** The value. */
		private V value;

		/**
		 * Instantiates a new simple cache task.
		 *
		 * @param key the key
		 * @param value the value
		 */
		public SimpleCacheTask(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.async.CacheTask#getKey()
		 */
		public K getKey() {
			return key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.async.CacheTask#getValue()
		 */
		public V getValue() {
			return value;
		}

	}
}
