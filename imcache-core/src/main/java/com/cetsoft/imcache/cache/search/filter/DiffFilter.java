/*
* Copyright (C) 2015 Cetsoft, http://www.cetsoft.com
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

import java.util.List;

/**
 * The Class DiffFilter.
 */
public class DiffFilter extends LogicalFilter {

	/** The right filter. */
	private Filter leftFilter, rightFilter;

	/**
	 * Instantiates a new diff filter.
	 *
	 * @param leftFilter the left filter
	 * @param rightFilter the right filter
	 */
	public DiffFilter(Filter leftFilter, Filter rightFilter) {
		this.leftFilter = leftFilter;
		this.rightFilter = rightFilter;
	}

	/**
	 * Gets the left filter.
	 *
	 * @return the left filter
	 */
	public Filter getLeftFilter() {
		return leftFilter;
	}

	/**
	 * Gets the right filter.
	 *
	 * @return the right filter
	 */
	public Filter getRightFilter() {
		return rightFilter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.filter.Filter#filter(java.util.List)
	 */
	public List<Object> filter(List<Object> objects) {
		List<Object> leftResult = leftFilter.filter(objects);
		List<Object> rightResult = rightFilter.filter(objects);
		for (Object object : rightResult) {
			leftResult.remove(object);
		}
		return leftResult;
	}

}
