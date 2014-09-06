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
* Date   : Nov 8, 2013
*/
package com.cetsoft.imcache.cache.search.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The Class OrFilter.
 */
public class OrFilter extends LogicalFilter {
	
	/** The filters. */
	private Filter[] filters;
	
	/**
	 * Instantiates a new or filter.
	 *
	 * @param filters the filters
	 */
	public OrFilter(Filter ... filters){
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

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.filter.Filter#filter(java.util.List)
	 */
	public List<Object> filter(List<Object> objects) {
		Set<Object> results = new HashSet<Object>();
		for (Filter filter : filters) {
			List<Object> result = filter.filter(objects);
			results.addAll(result);
		}
		return new ArrayList<Object>(results);
	}
}
