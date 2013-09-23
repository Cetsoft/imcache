package com.cetsoft.imcache.test;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.builder.CacheBuilder;

public class CacheBuilderTest {

	public static void main (String [] args){
		Cache<Integer,Integer> cache = CacheBuilder.heapCache().cacheLoader(new CacheLoader<Integer, Integer>() {
			public Integer load(Integer key) {
				return null;
			}
		}).capacity(10000).build();
		cache.get(0);
	}
}
