/*
 * Copyright (C) 2015 Cetsoft, http://www.cetsoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Author : Yusuf Aytas
 * Date   : Aug 14, 2015
 */
package com.cetsoft.imcache.redis;

import com.cetsoft.imcache.cache.AbstractCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.CacheStats;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.concurrent.ConcurrentCacheStats;
import com.cetsoft.imcache.redis.client.Client;
import com.cetsoft.imcache.redis.client.ConnectionException;
import com.cetsoft.imcache.serialization.Serializer;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * The Class RedisCache is a cache that uses redis server. to store or retrieve
 * data by serializing items into bytes. To do so, RedisCache uses a redis
 * client to talk to redis server. Any operation within this cache is a command
 * to redis.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class RedisCache<K, V> extends AbstractCache<K, V> {
    
    /** The client. */
    final Client client;

    /** The serializer. */
    final Serializer<Object> serializer;

    final ConcurrentCacheStats stats = new ConcurrentCacheStats();

    /**
     * Instantiates a new redis cache.
     *
     * @param cacheLoader the cache loader
     * @param evictionListener the eviction listener
     * @param serializer the serializer
     * @param client the client
     */
    public RedisCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener,
            Serializer<Object> serializer, Client client) {
        super(cacheLoader, evictionListener);
        this.client = client;
        this.serializer = serializer;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.cetsoft.imcache.cache.Cache#put(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void put(final K key, final V value) {
        try {
            client.set(serializer.serialize(key), serializer.serialize(value));
        } catch (ConnectionException e) {
            throw new RedisCacheException(e);
        } catch (IOException e) {
            throw new RedisCacheException(e);
        }
    }

    @Override
    public void put(final K key, final V value, final TimeUnit timeUnit, final long duration) {
        try {
            client.set(serializer.serialize(key), serializer.serialize(value), TimeUnit.MILLISECONDS.toMillis(duration));
        } catch (ConnectionException e) {
            throw new RedisCacheException(e);
        } catch (IOException e) {
            throw new RedisCacheException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cetsoft.imcache.cache.Cache#get(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public V get(final K key) {
        try {
            final byte[] serializedKey = serializer.serialize(key);
            V value = (V) serializer.deserialize(client.get(serializedKey));
            if (value == null) {
                stats.incrementMissCount();
                value = cacheLoader.load(key);
                if(value != null) {
                    byte[] serializedValue = serializer.serialize(value);
                    client.set(serializedKey, serializedValue);
                    stats.incrementLoadCount();
                }
            } else {
                stats.incrementHitCount();
            }
            return value;
        } catch (ConnectionException e) {
            throw new RedisCacheException(e);
        } catch (IOException e) {
            throw new RedisCacheException(e);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.cetsoft.imcache.cache.Cache#invalidate(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public V invalidate(final K key) {
        try {
            byte[] serializedKey = serializer.serialize(key);
            byte[] serializedValue = client.expire(serializedKey);
            V value = (V) serializer.deserialize(serializedValue);
            evictionListener.onEviction(key, value);
            stats.incrementEvictionCount();
            return value;
        } catch (ConnectionException e) {
            throw new RedisCacheException(e);
        } catch (IOException e) {
            throw new RedisCacheException(e);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.cetsoft.imcache.cache.Cache#contains(java.lang.Object)
     */
    @Override
    public boolean contains(final K key) {
        return get(key) != null;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.cetsoft.imcache.cache.Cache#clear()
     */
    @Override
    public void clear() {
        try {
            client.flushdb();
        } catch (ConnectionException e) {
            throw new RedisCacheException(e);
        } catch (IOException e) {
            throw new RedisCacheException(e);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.cetsoft.imcache.cache.Cache#size()
     */
    @Override
    public long size() {
        try {
            return client.dbsize();
        } catch (ConnectionException e) {
            throw new RedisCacheException(e);
        } catch (IOException e) {
            throw new RedisCacheException(e);
        }
    }

    @Override
    public CacheStats stats() {
        return stats;
    }

}
