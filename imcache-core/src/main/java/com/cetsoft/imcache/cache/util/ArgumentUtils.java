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
package com.cetsoft.imcache.cache.util;

/**
 * The type Argument utils.
 */
public class ArgumentUtils {

  /**
   * Check not null.
   *
   * @param reference the reference
   * @param msg the msg
   */
  public static void checkNotNull(final Object reference, final String msg) {
    if (reference == null) {
      throw new NullPointerException(msg);
    }
  }

  /**
   * Check positive.
   *
   * @param number the number
   * @param msg the msg
   */
  public static void checkPositive(long number, final String msg) {
    if (number < 1) {
      throw new IllegalArgumentException(msg);
    }
  }

  /**
   * Check not empty.
   *
   * @param str the str
   * @param msg the msg
   */
  public static void checkNotEmpty(final String str, final String msg) {
    checkNotNull(str, msg);
    if (str.isEmpty()) {
      throw new IllegalArgumentException(msg);
    }
  }
}
