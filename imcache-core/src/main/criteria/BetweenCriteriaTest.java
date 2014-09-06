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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.search.index.CacheIndex;

/**
 * The Class BetweenCriteriaTest.
 */
public class BetweenCriteriaTest {
	
	/** The upper bound. */
	@Mock
	Object lowerBound, upperBound;
	
	/** The result. */
	@Mock
	List<Object> result;
	
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
		BetweenCriteria betweenCriteria = new BetweenCriteria("a", lowerBound, upperBound);
		doReturn(result).when(cacheIndex).between(lowerBound, upperBound);
		List<Object> actualResult = betweenCriteria.meets(cacheIndex);
		verify(cacheIndex).between(lowerBound, upperBound);
		assertEquals(result, actualResult);
	}
	
}
