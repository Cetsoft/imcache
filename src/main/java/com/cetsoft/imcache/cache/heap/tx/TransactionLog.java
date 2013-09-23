package com.cetsoft.imcache.cache.heap.tx;

public interface TransactionLog {
	void apply();
	void rollback();
}
