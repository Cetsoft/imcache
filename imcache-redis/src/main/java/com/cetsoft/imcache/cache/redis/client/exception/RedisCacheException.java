package com.cetsoft.imcache.cache.redis.client.exception;


public class RedisCacheException extends RuntimeException {

	private static final long serialVersionUID = 7379846444846442221L;

	public RedisCacheException(String message) {
        super(message);
    }

    public RedisCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisCacheException(Throwable cause) {
        super(cause);
    }
}
