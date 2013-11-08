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
	public DiffFilter(Filter leftFilter, Filter rightFilter){
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

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.filter.Filter#filter(java.util.List)
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
