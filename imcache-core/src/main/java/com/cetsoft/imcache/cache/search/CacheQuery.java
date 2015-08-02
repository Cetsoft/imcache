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
* Date   : Sep 29, 2013
*/
package com.cetsoft.imcache.cache.search;

import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.filter.Filter;

/**
 * The Class CacheQuery is a base class for creating cache queries.
 */
public class CacheQuery implements Query {

	/** The criterias. */
	private Criteria criteria;

	private Filter filter;

	/**
	 * Instantiates a new cache query.
	 */
	private CacheQuery() {
	}

	/**
	 * New instance.
	 *
	 * @return the query
	 */
	public static Query newQuery() {
		return new CacheQuery();
	}

	public Query setCriteria(Criteria criteria) {
		this.criteria = criteria;
		return this;
	}

	public Criteria getCriteria() {
		return this.criteria;
	}

	public Query setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}

	public Filter getFilter() {
		return this.filter;
	}

}
