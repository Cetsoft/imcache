package com.cetsoft.imcache.spring;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;

public class ImcacheCacheManagerTest {

	
	@Mock
	CacheBuilder builder;
	
	@Mock
	SearchableCache<Object, Object> cache;
	
	ImcacheCacheManager cacheManager;
	
	String cacheName = "cache";
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		doReturn(cache).when(builder).build(cacheName);
		cacheManager = new ImcacheCacheManager(builder);
	}
	
	@Test
	public void getCache(){
		assertEquals(cache, cacheManager.getCache(cacheName).getNativeCache());
	}
	
	@Test
	public void getCacheNames(){
		cacheManager.getCache(cacheName);
		assertEquals(1, cacheManager.getCacheNames().size());
		assertTrue(cacheManager.getCacheNames().contains(cacheName));
	}
	
	@Test
	public void setGetCacheBuilder(){
		cacheManager = new ImcacheCacheManager();
		cacheManager.setCacheBuilder(builder);
		assertEquals(builder, cacheManager.getCacheBuilder());
	}
	
}
