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
* Date   : Nov 1, 2013
*/
package com.cetsoft.imcache.cache.search.criteria;

/**
 * The Class CriteriaBase.
 */
public abstract class CriteriaBase implements Criteria{

	/** The attribute name. */
	private String attributeName;
	
	/** The expected value. */
	protected Object value;
	
	/**
	 * Instantiates a new equals to criteria.
	 *
	 * @param attributeName the attribute name
	 * @param value the expected value
	 */
	public CriteriaBase(String attributeName, Object value){
		this.attributeName = attributeName;
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.criteria.Criteria#getAttributeName()
	 */
	public String getAttributeName() {
		return attributeName;
	}
}
