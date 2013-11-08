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
* Date   : Sep 28, 2013
*/
package com.cetsoft.imcache.cache.search;

import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.filter.Filter;

/**
 * The Interface Query is a piece of code (a query) that is sent to
 * a cache in order to get information back from the cache.
 * It is used as the way of retrieving the information from cache. 
 */
public interface Query{
	
	/**
	 * Adds the criteria.
	 *
	 * @param criteria the criteria
	 * @return the query
	 */
	Query setCriteria(Criteria criteria);
	
	/**
	 * Returns list of Criterias.
	 *
	 * @return the list
	 */
	Criteria getCriteria();
	
	Query setFilter(Filter filter);
	
	Filter getFilter();
}
