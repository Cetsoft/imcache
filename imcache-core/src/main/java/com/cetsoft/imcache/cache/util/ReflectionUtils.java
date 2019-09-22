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

import com.cetsoft.imcache.cache.search.AttributeException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Reflection utils.
 */
public class ReflectionUtils {

  private static final Map<String, Field> fields = new ConcurrentHashMap<>();

  /**
   * Gets field value.
   *
   * @param <T> the type parameter
   * @param fieldName the field name
   * @param value the value
   * @return the field value
   */
  public static <T> Object getFieldValue(final String fieldName, final T value)
  {
    try {
      final String fieldDiscriminator = value.getClass() + "." + fieldName;
      return fields.computeIfAbsent(fieldDiscriminator, (a) -> {
        try {
          final Field field = value.getClass().getDeclaredField(fieldName);
          field.setAccessible(true);
          return field;
        } catch (NoSuchFieldException e) {
          throw new AttributeException(e);
        }
      }).get(value);
    } catch (IllegalAccessException e) {
      throw new AttributeException(e);
    }
  }
}
