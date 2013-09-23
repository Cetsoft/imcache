package com.cetsoft.imcache.test;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.cache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.cache.heap.tx.Transaction;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;

public class HeapCacheTransactionTest {

	public static void main(String []args) {
		TransactionalHeapCache<Integer, Integer> cache = new TransactionalHeapCache<Integer, Integer>(new TransactionCommitter<Integer, Integer>() {
			public void doPut(Integer key, Integer value) {
				System.out.println("key["+key+"],"+"value["+value+"]");
			}
		}, new CacheLoader<Integer, Integer>() {public Integer load(Integer key) {return null;}}, 
		   new EvictionListener<Integer, Integer>() {public void onEviction(Integer key, Integer value) {}}, 
		   1000);
		Transaction transaction = CacheTransaction.get();
		transaction.begin();
		cache.put(3, 5);
		cache.put(10, 14);
		transaction.commit();
	}
}
