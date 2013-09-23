package com.cetsoft.imcache.cache.heap;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.heap.tx.TransactionException;
import com.cetsoft.imcache.cache.heap.tx.TransactionLog;

public class TransactionalHeapCache<K, V> extends HeapCache<K, V> {
	
	TransactionCommitter<K, V> committer;

	public TransactionalHeapCache(TransactionCommitter<K, V> committer,CacheLoader<K, V> cacheLoader, 
			EvictionListener<K, V> evictionListener, int capacity) {
		super(cacheLoader, evictionListener, capacity);
		this.committer = committer;
	}

	public void put(K key, V value) {
		V exValue = super.get(key);
		super.put(key, value);
		TransactionLog log = new PutTransactionLog<K, V>(this, committer, key, value, exValue);
		CacheTransaction.addLog(log);
	}

	public double hitRatio() {
		return super.hitRatio();
	}
	
	private static class PutTransactionLog<K,V> implements TransactionLog{

		private K key;
		private V value;
		private V exValue;
		private TransactionalHeapCache<K,V> cache;
		private TransactionCommitter<K, V> committer;
		
		public PutTransactionLog(TransactionalHeapCache<K,V> cache,TransactionCommitter<K, V> committer, 
				K key, V value, V exValue){
			this.key = key;
			this.cache = cache;
			this.value = value;
			this.exValue = exValue;
			this.committer = committer;
		}
		
		public void rollback() {
			cache.put(key, exValue);
		}

		public void apply() {
			try{
				committer.doPut(key, value);
			}catch(Exception exception){
				throw new TransactionException(exception);
			}
		}
		
	}
	
}
