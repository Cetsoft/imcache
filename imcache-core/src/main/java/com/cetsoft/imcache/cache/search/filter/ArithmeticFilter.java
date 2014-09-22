/*
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
* 
* Author : Yusuf Aytas
* Date   : Nov 8, 2013
*/
package com.cetsoft.imcache.cache.search.filter;

import java.lang.reflect.Field;

import com.cetsoft.imcache.cache.search.AttributeException;

/**
 * The Class ArithmeticFilter.
 */
public abstract class ArithmeticFilter extends LogicalFilter {

	/** The attribute name. */
	private String attributeName;

	/** The expected value. */
	protected Object value;

	/**
	 * Instantiates a new equals to filter.
	 * 
	 * @param attributeName
	 *            the attribute name
	 * @param value
	 *            the expected value
	 */
	public ArithmeticFilter(String attributeName, Object value) {
		this.attributeName = attributeName;
		this.value = value;
	}

	/**
	 * Gets the attribute name.
	 *
	 * @return the attribute name
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Gets the indexed key.
	 *
	 * @param object the object
	 * @return the indexed key
	 */
	protected Object getAttributeValue(Object object) {
		try {
			Field field = object.getClass().getDeclaredField(attributeName);
			field.setAccessible(true);
			return field.get(object);
		} catch (Exception e) {
			throw new AttributeException(e);
		}
	}

}
