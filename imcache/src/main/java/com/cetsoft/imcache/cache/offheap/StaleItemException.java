package com.cetsoft.imcache.cache.offheap;

public class StaleItemException extends RuntimeException {

	private static final long serialVersionUID = 5688389152095338751L;
	
	public StaleItemException(int expectedVersion, int actualVersion) {
		super("Expected version for the item is "+expectedVersion+" but the actual value was "+actualVersion);
	}

}
