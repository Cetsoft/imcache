package com.cetsoft.imcache.cache.redis;

import com.cetsoft.imcache.cache.AbstractCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;

public class RedisCache<K, V> extends AbstractCache<K, V> {

    public RedisCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener, IndexHandler<K, V> indexHandler) {
        super(cacheLoader, evictionListener, indexHandler);
    }

    @Override
    public void put(K key, V value) {
    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public V invalidate(K key) {
        return null;
    }

    @Override
    public boolean contains(K key) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public double hitRatio() {
        return 0;
    }
}
