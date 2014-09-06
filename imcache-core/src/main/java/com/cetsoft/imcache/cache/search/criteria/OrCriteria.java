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
package com.cetsoft.imcache.cache.search.criteria;

/**
 * The Class AndCriteria is used to retrieve items
 * union for the criterias.
 */
public class OrCriteria extends LogicalCriteria {

	/** The criterias. */
	private Criteria[] criterias;

	/**
	 * Instantiates a new or criteria.
	 *
	 * @param criterias the criterias
	 */
	public OrCriteria(Criteria... criterias) {
		this.criterias = criterias;
	}

	/**
	 * Gets the criterias.
	 *
	 * @return the criterias
	 */
	public Criteria[] getCriterias() {
		return criterias;
	}
}
