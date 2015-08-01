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

import java.util.ArrayList;
import java.util.List;

/**
 * The Class AndFilter.
 */
public class AndFilter extends LogicalFilter {

	/** The filters. */
	private Filter[] filters;

	/**
	 * Instantiates a new and filter.
	 *
	 * @param filters the filters
	 */
	public AndFilter(Filter... filters) {
		this.filters = filters;
	}

	/**
	 * Gets the filters.
	 *
	 * @return the filters
	 */
	public Filter[] getFilters() {
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.filter.Filter#filter(java.util.List)
	 */
	public List<Object> filter(List<Object> objects) {
		List<Object> results = new ArrayList<Object>();
		for (Filter filter : filters) {
			List<Object> result = filter.filter(objects);
			if (results.size() == 0) {
				results.addAll(result);
			} else {
				List<Object> mergedResults = new ArrayList<Object>(results.size());
				for (Object object : result) {
					if (results.contains(object)) {
						mergedResults.add(object);
					}
				}
				results = mergedResults;
			}
		}
		return results;
	}
}
