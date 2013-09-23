package com.cetsoft.imcache.cache.heap.tx;

public interface TransactionCommitter<K,V> {
	void doPut(K key, V value);
}
