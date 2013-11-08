/*
* Copyright (C) 2013 Cetsoft, http://www.cetsoft.com
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Library General Public
* License as published by the Free Software Foundation; either
* version 2 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Library General Public License for more details.
*
* You should have received a copy of the GNU Library General Public
* License along with this library; if not, write to the Free
* Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
* 
* Author : Yusuf Aytas
* Date   : Nov 1, 2013
*/
package com.cetsoft.imcache.cache.search.criteria;

import java.util.List;

import com.cetsoft.imcache.cache.search.index.CacheIndex;

/**
 * The Class GTETCriteria is used to retrieve items
 * greater than equals to the given value.
 */
public class GTETCriteria extends ArithmeticCriteria {

	/**
	 * Instantiates a new gTET criteria.
	 *
	 * @param attributeName the attribute name
	 * @param expectedValue the expected value
	 */
	public GTETCriteria(String attributeName, Object value) {
		super(attributeName, value);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.criteria.Criteria#meets(com.cetsoft.imcache.cache.search.index.CacheIndex)
	 */
	public List<Object> meets(CacheIndex cacheIndex) {
		return cacheIndex.greaterThanOrEqualsTo(value);
	}

}
