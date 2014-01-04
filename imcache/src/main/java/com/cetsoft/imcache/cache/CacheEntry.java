package com.cetsoft.imcache.cache;

public interface CacheEntry<K, V> {
	K getKey();
	V getValue();
}
