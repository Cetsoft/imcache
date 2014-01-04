package com.cetsoft.imcache.cache.pupulator;

import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;

public abstract class SimpleCachePupulator<K, V> extends AbstractCachePopulator<K, V>{
	
	public SimpleCachePupulator(Cache<K, V> cache) {
		super(cache);
	}

	public void pupulate() {
		List<CacheEntry<K, V>> entries = loadEntries();
		for (CacheEntry<K, V> cacheEntry : entries) {
			cache.put(cacheEntry.getKey(), cacheEntry.getValue());
		}
	}

}
