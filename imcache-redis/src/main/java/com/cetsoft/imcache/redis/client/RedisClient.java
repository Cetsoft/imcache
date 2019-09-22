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
package com.cetsoft.imcache.redis.client;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The Class RedisClient.
 */
public class RedisClient implements Client {

  /**
   * The Constant STATUS_OK.
   */
  private static final String STATUS_OK = "OK";

  /**
   * The command result.
   */
  final CommandResult commandResult;

  /**
   * The command executor.
   */
  final CommandExecutor commandExecutor;

  final Transaction transaction = new RedisTransaction();

  RedisClient(final RedisCommandExecutor redisCommandExecutor,
      final RedisCommandResult redisCommandResult) {
    this.commandExecutor = redisCommandExecutor;
    this.commandResult = redisCommandResult;
  }

  /**
   * Instantiates a new redis client.
   *
   * @param connection the connection
   */
  public RedisClient(final Connection connection) {
    this.commandExecutor = new RedisCommandExecutor(connection);
    this.commandResult = new RedisCommandResult(connection);
  }

  /**
   * Instantiates a new redis client.
   */
  public RedisClient() {
    this(new Connection());
  }

  /**
   * Instantiates a new redis client.
   *
   * @param host the host
   */
  public RedisClient(final String host) {
    this(new Connection(host));
  }

  /**
   * Instantiates a new redis client.
   *
   * @param host the host
   * @param port the port
   */
  public RedisClient(final String host, final int port) {
    this(new Connection(host, port));
  }

  /**
   * Runs a void command.
   *
   * @param command the command
   * @param args the args
   * @throws ConnectionException the connection exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  protected void runVoidCommand(final ByteCommand command, final byte[]... args)
      throws ConnectionException, IOException {
    transaction.open();
    try {
      commandExecutor.execute(command, args);
      final String status = commandResult.getStatus();
      if (!status.equals(STATUS_OK)) {
        throw new ConnectionException("Command couldn't run successfully " + command.toString());
      }
    } finally {
      transaction.close();
    }
  }


  @Override
  public void ping() throws ConnectionException, IOException {
    runVoidCommand(RedisCommands.PING);
  }


  @Override
  public void set(final byte[] key, final byte[] value) throws ConnectionException, IOException {
    runVoidCommand(RedisCommands.SET, key, value);
  }


  @Override
  public void set(final byte[] key, final byte[] value, final long expiryInMillis)
      throws ConnectionException, IOException {
    transaction.open();
    try {
      commandExecutor.execute(RedisCommands.SET, key, value);
      final String setStatus = commandResult.getStatus();
      if (!setStatus.equals(STATUS_OK)) {
        throw new ConnectionException("Command couldn't run successfully " + setStatus);
      }
      //redis don't accept expiry in milliseconds, so converting to milliseconds.
      commandExecutor.execute(RedisCommands.PEXPIRE, key, longToBytes(expiryInMillis));
      final String expireStatus = commandResult.getStatus();
      if (!setStatus.equals(STATUS_OK)) {
        throw new ConnectionException("Command couldn't run successfully " + expireStatus);
      }
    } finally {
      transaction.close();
    }
  }

  public byte[] longToBytes(final long longToBeConverted) {
    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(longToBeConverted);
    return buffer.array();
  }


  @Override
  public byte[] get(final byte[] key) throws ConnectionException, IOException {
    transaction.open();
    try {
      commandExecutor.execute(RedisCommands.GET, key);
      return commandResult.getBytes();
    } finally {
      transaction.close();
    }
  }


  @Override
  public byte[] expire(final byte[] key) throws ConnectionException, IOException {
    transaction.open();
    try {
      final byte[] value = get(key);
      commandExecutor.execute(RedisCommands.EXPIRE, key, new byte[]{'0'});
      commandResult.getInt();
      return value;
    } finally {
      transaction.close();
    }
  }


  @Override
  public void flushdb() throws ConnectionException, IOException {
    runVoidCommand(RedisCommands.FLUSHDB);
  }


  @Override
  public int dbsize() throws ConnectionException, IOException {
    transaction.open();
    try {
      commandExecutor.execute(RedisCommands.DBSIZE);
      return commandResult.getInt();
    } finally {
      transaction.close();
    }
  }

}
