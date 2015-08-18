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
* Date   : Aug 18, 2015
*/
package com.cetsoft.imcache.cache.builder;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.redis.RedisCache;
import com.cetsoft.imcache.redis.client.Client;
import com.cetsoft.imcache.redis.client.Connection;
import com.cetsoft.imcache.redis.client.MultiRedisClient;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class RedisCacheBuilder.
 */
public class RedisCacheBuilder extends AbstractCacheBuilder{
	
	/** The port. */
	private int port = Connection.DEFAULT_PORT;
	
	/** The host name. */
	private String hostName = Connection.DEFAULT_HOST;
	
	/** The concurrency level. */
	private int concurrencyLevel = MultiRedisClient.CONCURRENCY_LEVEL;
	
	/** The serializer. */
	private Serializer<Object> serializer = AbstractCacheBuilder.SERIALIZER;

	/**
	 * Cache loader.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param cacheLoader the cache loader
	 * @return the heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> RedisCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader) {
		this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
		return this;
	}

	/**
	 * Eviction listener.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param evictionListener the eviction listener
	 * @return the heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> RedisCacheBuilder evictionListener(EvictionListener<K, V> evictionListener) {
		this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
		return this;
	}
	
	/**
	 * Serializer.
	 *
	 * @param <V> the value type
	 * @param serializer the serializer
	 * @return the off heap cache builder
	 */
	public <V> RedisCacheBuilder serializer(Serializer<Object> serializer) {
		this.serializer = (Serializer<Object>) serializer;
		return this;
	}
	
	/**
	 * Host name.
	 *
	 * @param hostName the host name
	 * @return the redis cache builder
	 */
	public RedisCacheBuilder hostName(String hostName){
		this.hostName = hostName;
		return this;
	}
	
	/**
	 * Port.
	 *
	 * @param port the port
	 * @return the redis cache builder
	 */
	public RedisCacheBuilder port(int port){
		this.port = port;
		return this;
	}
	
	/**
	 * Concurrency level.
	 *
	 * @param concurrencyLevel the concurrency level
	 * @return the redis cache builder
	 */
	public RedisCacheBuilder concurrencyLevel(int concurrencyLevel){
		this.concurrencyLevel = concurrencyLevel;
		return this;
	} 
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.builder.AbstractCacheBuilder#build()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <K, V> Cache<K, V> build() {
		Client client = new MultiRedisClient(hostName, port, concurrencyLevel);
		return new RedisCache<K, V>((CacheLoader<K, V>)cacheLoader, (EvictionListener<K, V>)evictionListener, 
				serializer, client);
	}

}
