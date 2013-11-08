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
	public DiffCriteria(Criteria leftCriteria, Criteria rightCriteria){
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
