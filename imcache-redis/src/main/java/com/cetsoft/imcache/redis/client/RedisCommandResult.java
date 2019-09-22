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

/**
 * The Class RedisCommandResult.
 */
public class RedisCommandResult implements CommandResult {

  /**
   * The connection.
   */
  private final Connection connection;

  /**
   * Instantiates a new redis command result.
   *
   * @param connection the connection
   */
  public RedisCommandResult(final Connection connection) {
    this.connection = connection;
  }


  @Override
  public byte[] getBytes() throws ConnectionException, IOException {
    final RedisStreamReader streamReader = getStreamReader();
    checkMessageType(streamReader, RedisBytes.DOLLAR_BYTE);
    int length = streamReader.readInt();
    return streamReader.read(length);
  }


  @Override
  public String getStatus() throws ConnectionException, IOException {
    final RedisStreamReader streamReader = getStreamReader();
    checkMessageType(streamReader, RedisBytes.PLUS_BYTE);
    return streamReader.readString();
  }


  @Override
  public int getInt() throws ConnectionException, IOException {
    final RedisStreamReader streamReader = getStreamReader();
    checkMessageType(streamReader, RedisBytes.COLON_BYTE);
    return streamReader.readInt();
  }

  /**
   * Checks message type received. If it's unexpected throws an exception.
   *
   * @param streamReader the stream reader
   * @param expectedByte the expected byte
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ConnectionException the connection exception
   */
  protected void checkMessageType(final RedisStreamReader streamReader, final byte expectedByte)
      throws IOException,
      ConnectionException {
    final byte actualByte = streamReader.readByte();
    if (actualByte != expectedByte) {
      throw new ConnectionException(
          "Expected(" + ((char) expectedByte) + "), Found(" + ((char) actualByte)
              + ").");
    }
  }

  /**
   * Gets the stream reader.
   *
   * @return the input stream
   * @throws ConnectionException the connection exception
   */
  protected RedisStreamReader getStreamReader() throws ConnectionException, IOException {
    connection.open();
    return connection.getStreamReader();
  }

}
