/**
 * Copyright © 2013 Cetsoft. All rights reserved.
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
 */
package com.cetsoft.imcache.redis.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class MultiRedisClient is a container for multiple clients. Upon initialization, it creates n
 * client where n equals to concurrencyLevel. This class delegates each method call to clients that
 * it created.
 */
public class MultiRedisClient implements Client {

  /**
   * The Constant CONCURRENCY_LEVEL.
   */
  public static final int CONCURRENCY_LEVEL = 3;

  /**
   * The redis clients.
   */
  final List<Client> redisClients;

  final AtomicInteger currentIndex = new AtomicInteger(0);

  /**
   * Instantiates a new multi redis client.
   *
   * @param host the host
   * @param port the port
   */
  public MultiRedisClient(final String host, final int port) {
    this(host, port, CONCURRENCY_LEVEL);
  }

  /**
   * Instantiates a new multi redis client.
   *
   * @param host the host
   * @param port the port
   * @param concurrencyLevel the concurrency level
   */
  public MultiRedisClient(final String host, final int port, final int concurrencyLevel) {
    if (concurrencyLevel < 1) {
      throw new IllegalArgumentException(
          "Concurrency level must be greater or equals " + "to 1 but it was "
              + concurrencyLevel);
    }
    redisClients = new ArrayList<>(concurrencyLevel);
    for (int i = 0; i < concurrencyLevel; i++) {
      final Connection connection = new Connection(host, port);
      final Client client = new RedisClient(connection);
      redisClients.add(client);
    }
  }


  @Override
  public void ping() throws ConnectionException, IOException {
    getClient().ping();
  }


  @Override
  public void set(byte[] key, byte[] value) throws ConnectionException, IOException {
    getClient().set(key, value);
  }


  @Override
  public void set(byte[] key, byte[] value, long expiryInMillis)
      throws ConnectionException, IOException {
    getClient().set(key, value, expiryInMillis);
  }


  @Override
  public byte[] get(byte[] key) throws ConnectionException, IOException {
    return getClient().get(key);
  }


  @Override
  public byte[] expire(byte[] key) throws ConnectionException, IOException {
    return getClient().expire(key);
  }


  @Override
  public void flushdb() throws ConnectionException, IOException {
    getClient().flushdb();
  }


  @Override
  public int dbsize() throws ConnectionException, IOException {
    return getClient().dbsize();
  }

  /**
   * Gets a client from array of clients sequentially. We aim to distribute clients as evenly as
   * possible.
   *
   * @return the client
   */
  protected Client getClient() {
    final int index = currentIndex.incrementAndGet() % redisClients.size();
    return redisClients.get(index);
  }

}
