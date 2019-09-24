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

import org.junit.Test;

public class ArgumentUtilsTest {

  @Test(expected = NullPointerException.class)
  public void checkNotNull() {
    ArgumentUtils.checkNotNull(null, "not null");
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkPositive() {
    ArgumentUtils.checkPositive(-1, "positive");
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkPositiveZero() {
    ArgumentUtils.checkPositive(0, "positive");
  }

  @Test(expected = NullPointerException.class)
  public void checkNotEmpty() {
    ArgumentUtils.checkNotEmpty(null, "not null");
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkNotEmptyEmptyString() {
    ArgumentUtils.checkNotEmpty("", "not null");
  }

}
