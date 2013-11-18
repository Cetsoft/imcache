package com.cetsoft.imcache.cache;

public class ImcacheType<K,V> implements CacheType<K,V> {

	protected static volatile int ordinalCounter = 0;

	private final int ordinal;
	
	public ImcacheType() {
		ordinal = ordinalCounter++;
	}

	public int getType() {
		return ordinal;
	}

}
