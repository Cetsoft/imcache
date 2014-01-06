package com.cetsoft.imcache.examples;

import java.util.ArrayList;
import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.pupulator.ConcurrentCachePopulator;
import com.cetsoft.imcache.cache.util.CacheUtils;

public class CachePopulatorExample extends ConcurrentCachePopulator<String, String>{

	public CachePopulatorExample(Cache<String, String> cache) {
		super(cache);
	}

	@Override
	public List<CacheEntry<String, String>> loadEntries() {
		final int SIZE = 3;
		List<CacheEntry<String, String>> cacheEntries = new ArrayList<CacheEntry<String,String>>(SIZE);
		for (int i = 0; i < SIZE; i++) {
			cacheEntries.add(CacheUtils.createEntry(""+i,""+i));
		}
		return cacheEntries;
	}
	
	public static void main(String [] args){
		Cache<String, String> cache = CacheBuilder.concurrentHeapCache().build();
		CachePopulatorExample populatorExample = new CachePopulatorExample(cache);
		populatorExample.pupulate();
		System.out.println(cache.get("0"));
	}

}
