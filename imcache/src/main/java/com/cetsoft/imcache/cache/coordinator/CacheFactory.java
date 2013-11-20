package com.cetsoft.imcache.cache.coordinator;

import com.cetsoft.imcache.cache.Cache;

public interface CacheFactory{
	<K,V> Cache<K,V> create();
}