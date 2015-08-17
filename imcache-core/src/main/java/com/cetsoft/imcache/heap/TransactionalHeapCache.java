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
package com.cetsoft.imcache.heap;

import java.util.Map;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.heap.tx.TransactionException;
import com.cetsoft.imcache.heap.tx.TransactionLog;

/**
 * The Class TransactionalHeapCache provides caching items transactionally
 * which are persisted to the TransactionCommitter interface in case of a
 * commit.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class TransactionalHeapCache<K, V> extends HeapCache<K, V> {

	/** The committer. */
	TransactionCommitter<K, V> committer;

	/**
	 * Instantiates a new transactional heap cache.
	 *
	 * @param committer the committer
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the query executer
	 * @param capacity the capacity
	 */
	public TransactionalHeapCache(TransactionCommitter<K, V> committer, CacheLoader<K, V> cacheLoader,
			EvictionListener<K, V> evictionListener, IndexHandler<K, V> indexHandler, int capacity) {
		super(cacheLoader, evictionListener, indexHandler, capacity);
		this.committer = committer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.heap.HeapCache#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public void put(K key, V value) {
		V exValue = super.get(key);
		super.put(key, value);
		TransactionLog log = new PutTransactionLog<K, V>(cache, committer, key, value, exValue);
		CacheTransaction.addLog(log);
	}

	/**
	 * The Class PutTransactionLog.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	private static class PutTransactionLog<K, V> implements TransactionLog {

		/** The key. */
		private K key;

		/** The value. */
		private V value;

		/** The ex value. */
		private V exValue;

		/** The cache. */
		private Map<K, V> cache;

		/** The committer. */
		private TransactionCommitter<K, V> committer;

		/**
		 * Instantiates a new put transaction log.
		 *
		 * @param cache the cache
		 * @param committer the committer
		 * @param key the key
		 * @param value the value
		 * @param exValue the ex value
		 */
		public PutTransactionLog(Map<K, V> cache, TransactionCommitter<K, V> committer, K key, V value, V exValue) {
			this.key = key;
			this.cache = cache;
			this.value = value;
			this.exValue = exValue;
			this.committer = committer;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.heap.tx.TransactionLog#rollback()
		 */
		public void rollback() {
			cache.put(key, exValue);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.heap.tx.TransactionLog#apply()
		 */
		public void apply() {
			try {
				committer.doPut(key, value);
			} catch (Exception exception) {
				throw new TransactionException(exception);
			}
		}

	}

}
