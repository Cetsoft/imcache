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
package com.cetsoft.imcache.cache.search.index;

import java.util.List;

/**
 * The Class CacheIndexBase throws UnsupportedOperationException for all methods to be implemented.
 */
public abstract class CacheIndexBase implements CacheIndex {


  public List<Object> equalsTo(Object expectedValue) {
    throw new UnsupportedOperationException();
  }


  public List<Object> lessThan(Object value) {
    throw new UnsupportedOperationException();
  }


  public List<Object> lessThanOrEqualsTo(Object value) {
    throw new UnsupportedOperationException();
  }


  public List<Object> greaterThan(Object value) {
    throw new UnsupportedOperationException();
  }


  public List<Object> greaterThanOrEqualsTo(Object value) {
    throw new UnsupportedOperationException();
  }


  public List<Object> between(Object lowerBound, Object upperBound) {
    throw new UnsupportedOperationException();
  }

}
