/**
 * Copyright © 2013 Cetsoft. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cetsoft.imcache.cache.builder;

import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotEmpty;
import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotNull;
import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkPositive;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.redis.RedisCache;
import com.cetsoft.imcache.redis.client.Client;
import com.cetsoft.imcache.redis.client.Connection;
import com.cetsoft.imcache.redis.client.MultiRedisClient;
import com.cetsoft.imcache.serialization.Serializer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class RedisCacheBuilder.
 */
public class RedisCacheBuilder extends BaseCacheBuilder {

  private final AtomicInteger cacheNumber = new AtomicInteger();
  /**
   * The port.
   */
  private int port = Connection.DEFAULT_PORT;

  /**
   * The host name.
   */
  private String hostName = Connection.DEFAULT_HOST;

  /**
   * The concurrency level.
   */
  private int concurrencyLevel = MultiRedisClient.CONCURRENCY_LEVEL;

  public RedisCacheBuilder() {
    name = "imcache-redis-cache-" + cacheNumber.incrementAndGet();
  }

  /**
   * Name redis cache.
   *
   * @param name the name
   * @return the offheap cache builder
   */
  public RedisCacheBuilder name(final String name) {
    checkNotEmpty(name, "name can't be null or empty");
    this.name = name;
    return this;
  }

  /**
   * Cache loader.
   *
   * @param <K> the key type
   * @param <V> the value type
   * @param cacheLoader the cache loader
   * @return the heap cache builder
   */
  @SuppressWarnings("unchecked")
  public <K, V> RedisCacheBuilder cacheLoader(final CacheLoader<K, V> cacheLoader) {
    checkNotNull(cacheLoader, "cache loader can't be null");
    this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
    return this;
  }

  /**
   * Eviction listener.
   *
   * @param <K> the key type
   * @param <V> the value type
   * @param evictionListener the eviction listener
   * @return the redis cache builder
   */
  @SuppressWarnings("unchecked")
  public <K, V> RedisCacheBuilder evictionListener(final EvictionListener<K, V> evictionListener) {
    checkNotNull(evictionListener, "eviction listener can't be null");
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
  public <V> RedisCacheBuilder serializer(final Serializer<Object> serializer) {
    checkNotNull(serializer, "serializer can't be null");
    this.serializer = serializer;
    return this;
  }

  /**
   * Host name.
   *
   * @param hostName the host name
   * @return the redis cache builder
   */
  public RedisCacheBuilder hostName(final String hostName) {
    checkNotEmpty(hostName, "hostname can't be null or empty");
    this.hostName = hostName;
    return this;
  }

  /**
   * Port.
   *
   * @param port the port
   * @return the redis cache builder
   */
  public RedisCacheBuilder port(final int port) {
    checkPositive(port, "port number must be positive");
    this.port = port;
    return this;
  }

  /**
   * Concurrency level.
   *
   * @param concurrencyLevel the concurrency level
   * @return the redis cache builder
   */
  public RedisCacheBuilder concurrencyLevel(int concurrencyLevel) {
    checkPositive(concurrencyLevel, "concurrency level must be positive");
    this.concurrencyLevel = concurrencyLevel;
    return this;
  }

  @SuppressWarnings("unchecked")
  public <K, V> RedisCache<K, V> build() {
    Client client = new MultiRedisClient(hostName, port, concurrencyLevel);
    return new RedisCache<>(name, (CacheLoader<K, V>) cacheLoader,
        (EvictionListener<K, V>) evictionListener, serializer, client);
  }

  @SuppressWarnings("unchecked")
  public <K, V> RedisCache<K, V> build(final String cacheName) {
    return name(cacheName).build();
  }

}
