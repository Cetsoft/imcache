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
package com.cetsoft.imcache.cache.search.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GTFilter is used to retrieve items greater than the given value.
 */
public class GTFilter extends ArithmeticFilter {

  /**
   * Instantiates a new gT filter.
   *
   * @param attributeName the attribute name
   * @param value the value
   */
  public GTFilter(String attributeName, Object value) {
    super(attributeName, value);
  }


  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Object> filter(List<Object> objects) {
    final List<Object> result = new ArrayList<Object>(objects.size());
    for (final Object object : objects) {
      final Comparable objectValue = (Comparable) getAttributeValue(object);
      if (objectValue.compareTo(value) > 0) {
        result.add(object);
      }
    }
    return result;
  }

}
