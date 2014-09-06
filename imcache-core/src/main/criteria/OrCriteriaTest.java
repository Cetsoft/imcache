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
* Date   : Jun 2, 2014
*/
package com.cetsoft.imcache.cache.search.criteria;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.search.index.CacheIndex;

/**
 * The Class OrCriteriaTest.
 */
public class OrCriteriaTest {
	
	/** The criteria. */
	@Mock
	Criteria criteria;
	
	/** The cache index. */
	@Mock
	CacheIndex cacheIndex;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Gets the criterias.
	 *
	 * @return the criterias
	 */
	@Test
	public void getCriterias(){
		OrCriteria orCriteria = new OrCriteria(criteria, criteria);
		Criteria[] criterias = orCriteria.getCriterias();
		assertEquals(criteria, criterias[0]);
	}
	
}
