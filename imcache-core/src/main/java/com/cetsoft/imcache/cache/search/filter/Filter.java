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
 * The Interface Filter for meeting certain condition.
 */
public interface Filter {

	/**
	 * And.
	 *
	 * @param filter the filter
	 * @return the filter
	 */
	Filter and(Filter filter);

	/**
	 * Or.
	 *
	 * @param filter the filter
	 * @return the filter
	 */
	Filter or(Filter filter);

	/**
	 * Diff.
	 *
	 * @param filter the filter
	 * @return the filter
	 */
	Filter diff(Filter filter);

	/**
	 * Filter.
	 *
	 * @param objects the objects
	 * @return the list
	 */
	List<Object> filter(List<Object> objects);
}
