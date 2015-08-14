package com.cetsoft.imcache.redis;

import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.cache.AbstractCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.redis.client.Client;
import com.cetsoft.imcache.serialization.Serializer;

public class RedisCache<K, V> extends AbstractCache<K, V> {
	
	Client client;
	Serializer<Object> serializer;
	
	/** The hit. */
	protected AtomicLong hit = new AtomicLong();

	/** The miss. */
	protected AtomicLong miss = new AtomicLong();
	
	public RedisCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener,
			IndexHandler<K, V> indexHandler, Serializer<Object> serializer, Client client) {
		super(cacheLoader, evictionListener, indexHandler);
		this.client = client;
		this.serializer = serializer;
	}

	@Override
	public void put(K key, V value) {
		try {
			client.set(serializer.serialize(key), serializer.serialize(value));
		} catch (Exception e) {
			throw new RedisCacheException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(K key) {
		try {
			byte[] serializedKey = serializer.serialize(key);
			V value = (V) serializer.deserialize(client.get(serializedKey));
			if(value == null){
				miss.incrementAndGet();
				value = cacheLoader.load(key);
				byte[] serializedValue = serializer.serialize(value);
				client.set(serializedKey, serializedValue);
			}
			else{
				hit.incrementAndGet();
			}
			return value;
		} catch (Exception e) {
			throw new RedisCacheException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public V invalidate(K key) {
		try {
			byte[] serializedKey = serializer.serialize(key);
			byte[] serializedValue = client.expire(serializedKey);
			V value = (V) serializer.deserialize(serializedValue);
			evictionListener.onEviction(key, value);
			return value;
		} catch (Exception e) {
			throw new RedisCacheException(e);
		}
	}

	@Override
	public boolean contains(K key) {
		return get(key) != null;
	}

	@Override
	public void clear() {
		try {
			client.flushdb();
		} catch (Exception e) {
			throw new RedisCacheException(e);
		}
	}

	@Override
	public double hitRatio() {
		return hit.get() / (hit.get() +  miss.get());
	}

	@Override
	public int size() {
		try {
			return client.dbsize();
		} catch (Exception e) {
			throw new RedisCacheException(e);
		}
	}

}
