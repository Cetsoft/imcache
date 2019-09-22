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
 * The Class RedisCommandExecutor.
 */
public class RedisCommandExecutor implements CommandExecutor {

  /**
   * The connection.
   */
  final Connection connection;

  /**
   * Instantiates a new redis command executor.
   *
   * @param connection the connection
   */
  public RedisCommandExecutor(final Connection connection) {
    this.connection = connection;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.redis.client.CommandExecutor#execute(com.cetsoft.
   * imcache.redis.client.ByteCommand, byte[][])
   */
  public void execute(final ByteCommand command, final byte[]... args)
      throws ConnectionException, IOException {
    connection.open();
    final RedisStreamWriter streamWriter = connection.getStreamWriter();
    streamWriter.write(RedisBytes.ASTERISK_BYTE);
    streamWriter.write(args.length + 1);// 1 comes from set
    streamWriter.writeNewLine();
    streamWriter.write(RedisBytes.DOLLAR_BYTE);
    streamWriter.write(command.getBytes().length);
    streamWriter.writeNewLine();
    streamWriter.write(command.getBytes());
    streamWriter.writeNewLine();

    for (final byte[] arg : args) {
      streamWriter.write(RedisBytes.DOLLAR_BYTE);
      streamWriter.write(arg.length);
      streamWriter.writeNewLine();
      streamWriter.write(arg);
      streamWriter.writeNewLine();
    }
    streamWriter.flush();
  }

}
