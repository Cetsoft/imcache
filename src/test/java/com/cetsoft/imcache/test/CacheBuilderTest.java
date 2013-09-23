package com.cetsoft.imcache.test;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;

public class CacheBuilderTest {

	public static void main (String [] args){
		Cache<Integer,Integer> cache = CacheBuilder.heapCache().build();
		cache.get(0);
	}
}
