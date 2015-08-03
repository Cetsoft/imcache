package com.cetsoft.imcache.examples;

import org.junit.Test;

public class ExampleTests {
	
	@Test
	public void examples(){
		AsyncListenerExample.example();
		CacheCoordinatorExample.example();
		CachePopulatorExample.example();
		HeapCacheTransactionExample.example();
		MultiLevelCacheExample.example();
		QuerySearchExample.example();
		SpringCacheExample.example();
		SpringConfigurationExample.example();
		VersionedOffHeapCacheExample.example();
	}
}
