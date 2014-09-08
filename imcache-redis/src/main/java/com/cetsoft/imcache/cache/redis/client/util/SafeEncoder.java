package com.cetsoft.imcache.cache.redis.client.util;

import com.cetsoft.imcache.cache.redis.client.Protocol;
import com.cetsoft.imcache.cache.redis.client.exception.RedisCacheException;

import java.io.UnsupportedEncodingException;

public class SafeEncoder {
    public static byte[][] encodeMany(final String... strs) {
        byte[][] many = new byte[strs.length][];
        for (int i = 0; i < strs.length; i++) {
            many[i] = encode(strs[i]);
        }
        return many;
    }

    public static byte[] encode(final String str) {
        try {
            if (str == null) {
                throw new RedisCacheException(
                        "value sent to redis cannot be null");
            }
            return str.getBytes(Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RedisCacheException(e);
        }
    }

    public static String encode(final byte[] data) {
        try {
            return new String(data, Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RedisCacheException(e);
        }
    }
}