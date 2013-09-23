package com.cetsoft.imcache.cache.heap.tx;

public interface Transaction {
	void begin();
	void commit();
	void rollback();
	int getStatus();
}
