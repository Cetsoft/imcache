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
package com.cetsoft.imcache.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SimpleItemTest {

  @Test
  public void simpleItem() {
    SimpleItem<Integer> item = new SimpleItem<Integer>(10);
    assertEquals(10, item.getValue().intValue());
    assertEquals(0, item.getVersion());

    SimpleItem<Integer> item2 = new SimpleItem<>(2, 10);
    assertEquals(2, item2.getVersion());
    assertEquals(10, item2.getValue().intValue());

    assertEquals(item2.getVersion() + 1, item2.update(3).getVersion());
    assertFalse(item2.equals(item2.update(4)));
  }
}
