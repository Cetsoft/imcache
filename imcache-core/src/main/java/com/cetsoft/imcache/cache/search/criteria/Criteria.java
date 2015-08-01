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
package com.cetsoft.imcache.cache.search.criteria;

/**
 * The Interface Criteria for meeting certain condition.
 */
public interface Criteria {

	/**
	 * And.
	 *
	 * @param criteria the criteria
	 * @return the criteria
	 */
	Criteria and(Criteria criteria);

	/**
	 * Or.
	 *
	 * @param criteria the criteria
	 * @return the criteria
	 */
	Criteria or(Criteria criteria);

	/**
	 * Diff.
	 *
	 * @param criteria the criteria
	 * @return the criteria
	 */
	Criteria diff(Criteria criteria);
}
