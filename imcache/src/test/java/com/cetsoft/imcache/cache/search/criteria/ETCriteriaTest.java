/*
* Copyright (C) 2015 Cetsoft, http://www.cetsoft.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
 * The Class ETCriteriaTest.
 */
public class ETCriteriaTest {
	
	/** The expected value. */
	@Mock
	Object expectedValue;
	
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
	 * Meets.
	 */
	@Test
	public void meets(){
		ETCriteria criteria = new ETCriteria("a", expectedValue);
		doReturn(result).when(cacheIndex).equalsTo(expectedValue);
		List<Object> actualResult = criteria.meets(cacheIndex);
		verify(cacheIndex).equalsTo(expectedValue);
		assertEquals(result, actualResult);
	}
	
}
