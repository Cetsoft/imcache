package com.cetsoft.imcache.cache.pupulator;

import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;

public abstract class LazyCachePupulator<K,V> extends AbstractCachePopulator<K, V>{
	
	public LazyCachePupulator(Cache<K, V> cache) {
		super(cache);
	}

	public void pupulate() {
		new Thread(new Runnable() {
			public void run() {
				List<CacheEntry<K, V>> entries = loadEntries();
				for (CacheEntry<K, V> cacheEntry : entries) {
					cache.put(cacheEntry.getKey(), cacheEntry.getValue());
				}
			}
		}, "imcache:cachePopulator(name="+cache.getName()+",thread=0)").start();
	}

}
