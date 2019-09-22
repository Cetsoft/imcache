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

import java.io.UnsupportedEncodingException;

/**
 * The Enum RedisCommands are enum values of corresponding redis commands.
 */
public enum RedisCommands implements ByteCommand {

  PING, SET, GET, EXPIRE, PEXPIRE, FLUSHDB, DBSIZE;

  public static final String CHARSET = "UTF-8";

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.redis.client.ByteCommand#getBytes()
   */
  public byte[] getBytes() {
    try {
      return this.name().getBytes(CHARSET);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
