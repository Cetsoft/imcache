package com.cetsoft.imcache.redis;

public class RedisCacheException extends RuntimeException{

	public RedisCacheException(Exception exception) {
		super(exception);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5916280479309274446L;

}
