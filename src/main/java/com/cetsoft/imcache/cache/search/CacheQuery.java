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
* Date   : Sep 29, 2013
*/
package com.cetsoft.imcache.cache.search;

import java.util.ArrayList;
import java.util.List;

import com.cetsoft.imcache.cache.search.criteria.Criteria;

/**
 * The Class CacheQuery is a base class for creating cache queries.
 */
public class CacheQuery implements Query{
	
	/** The criterias. */
	List<Criteria> criterias = new ArrayList<Criteria>(1);
	
	/**
	 * Instantiates a new cache query.
	 */
	private CacheQuery(){}
	
	/**
	 * New instance.
	 *
	 * @return the query
	 */
	public static Query newQuery(){
		return new CacheQuery();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.Query#addCriteria(com.cetsoft.imcache.cache.search.criteria.Criteria)
	 */
	public Query addCriteria(Criteria criteria) {
		criterias.add(criteria);
		return this;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.Query#criterias()
	 */
	public List<Criteria> criterias() {
		return criterias;
	}

}
