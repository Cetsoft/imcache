/*
 * Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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
