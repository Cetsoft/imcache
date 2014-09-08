package com.cetsoft.imcache.cache.redis.client.exception;

public class RedisConnectionException extends RedisCacheException {

    public RedisConnectionException(String message) {
        super(message);
    }

    public RedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisConnectionException(Throwable cause) {
        super(cause);
    }
}
