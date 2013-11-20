/*
* Copyright (C) 2013 Cetsoft, http://www.cetsoft.com
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
package com.cetsoft.imcache.cache.heap;

import java.util.Map;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.heap.tx.TransactionException;
import com.cetsoft.imcache.cache.heap.tx.TransactionLog;
import com.cetsoft.imcache.cache.search.IndexHandler;

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
	public TransactionalHeapCache(TransactionCommitter<K, V> committer,CacheLoader<K, V> cacheLoader, 
			EvictionListener<K, V> evictionListener,IndexHandler<K, V> indexHandler, int capacity) {
		super(cacheLoader, evictionListener,indexHandler, capacity);
		this.committer = committer;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.heap.HeapCache#put(java.lang.Object, java.lang.Object)
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
	private static class PutTransactionLog<K,V> implements TransactionLog{

		/** The key. */
		private K key;
		
		/** The value. */
		private V value;
		
		/** The ex value. */
		private V exValue;
		
		/** The cache. */
		private Map<K,V> cache;
		
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
		public PutTransactionLog(Map<K,V> cache,TransactionCommitter<K, V> committer, 
				K key, V value, V exValue){
			this.key = key;
			this.cache = cache;
			this.value = value;
			this.exValue = exValue;
			this.committer = committer;
		}
		
		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.heap.tx.TransactionLog#rollback()
		 */
		public void rollback() {
			cache.put(key, exValue);
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.heap.tx.TransactionLog#apply()
		 */
		public void apply() {
			try{
				committer.doPut(key, value);
			}catch(Exception exception){
				throw new TransactionException(exception);
			}
		}
		
	}
	
}
