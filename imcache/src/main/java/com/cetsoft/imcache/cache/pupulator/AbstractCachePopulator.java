package com.cetsoft.imcache.cache.pupulator;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CachePopulator;

public abstract class AbstractCachePopulator<K,V> implements CachePopulator<K, V>{

	protected Cache<K,V> cache;
	
	public AbstractCachePopulator(Cache<K, V> cache) {
		this.cache = cache;
	}
}
