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
 * The Class DiffCriteria  is used to retrieve items
 * difference for the leftCriteria to rightCriteria.
 */
public class DiffCriteria extends LogicalCriteria {

	/** The right criteria. */
	private Criteria leftCriteria, rightCriteria;

	/**
	 * Instantiates a new diff criteria.
	 *
	 * @param leftCriteria the left criteria
	 * @param rightCriteria the right criteria
	 */
	public DiffCriteria(Criteria leftCriteria, Criteria rightCriteria) {
		this.leftCriteria = leftCriteria;
		this.rightCriteria = rightCriteria;
	}

	/**
	 * Gets the left criteria.
	 *
	 * @return the left criteria
	 */
	public Criteria getLeftCriteria() {
		return leftCriteria;
	}

	/**
	 * Gets the right criteria.
	 *
	 * @return the right criteria
	 */
	public Criteria getRightCriteria() {
		return rightCriteria;
	}

}
