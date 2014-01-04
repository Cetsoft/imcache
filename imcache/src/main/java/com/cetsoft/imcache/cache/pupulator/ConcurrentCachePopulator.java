package com.cetsoft.imcache.cache.pupulator;

import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;

public abstract class ConcurrentCachePopulator<K, V> extends AbstractCachePopulator<K, V>{

	private int concurrencyLevel;
	private final static int DEFAULT_CONCURRENCY_LEVEL = 11;
	
	public ConcurrentCachePopulator(Cache<K, V> cache, int concurrencyLevel) {
		super(cache);
		this.concurrencyLevel = concurrencyLevel;
	}
	
	public ConcurrentCachePopulator(Cache<K, V> cache) {
		this(cache, DEFAULT_CONCURRENCY_LEVEL);
	}

	public void pupulate() {
		final List<CacheEntry<K, V>> entries = loadEntries();
		for (int i = 0; i < concurrencyLevel; i++) {
			final int start = i*entries.size()/concurrencyLevel;
			final int stop = i==concurrencyLevel-1?i*entries.size()/concurrencyLevel:entries.size();
			new Thread(new Runnable() {
				public void run() {
					for (int j = start; j < stop; j++) {
						cache.put(entries.get(j).getKey(), entries.get(j).getValue());
					}
				}
			}, "imcache:cachePopulator(name="+cache.getName()+",thread="+i+")").start();
		}
	}

}
