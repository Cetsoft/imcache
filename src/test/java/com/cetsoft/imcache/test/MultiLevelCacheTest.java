package com.cetsoft.imcache.test;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.builder.CacheBuilder;

public class MultiLevelCacheTest {
	
	@SuppressWarnings("null")
	public static void main(String [] args){
		final CacheDao cacheDao = null;// This is just for example purposes.
		final Cache<String,String> offHeapCache = CacheBuilder.offHeapCache().cacheLoader(new CacheLoader<String, String>() {
			public String load(String key) {
				return cacheDao.load(key);
			}
		}).evictionListener(new EvictionListener<String, String>() {
			public void onEviction(String key, String value) {
				cacheDao.store(key, value);
			}
		}).build();
		Cache<String,String> multiLevelCache = CacheBuilder.heapCache().cacheLoader(new CacheLoader<String, String>() {
			public String load(String key) {
				return offHeapCache.get(key);
			}
		}).evictionListener(new EvictionListener<String, String>() {
			public void onEviction(String key, String value) {
				offHeapCache.put(key, value);
			}
		}).capacity(10000).build();
		multiLevelCache.put("red","apple");
	}
	
	public static interface CacheDao {
		String load(String key);
		void store(String key, String value);
	}
}
