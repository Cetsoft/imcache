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
* Date   : Jun 1, 2014
*/
package com.cetsoft.imcache.cache.search.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * The Class ArithmeticFilterTest.
 */
public class ArithmeticFilterTest {

	/** The arithmetic filter. */
	ArithmeticFilter arithmeticFilter;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Gets the attribute value test.
	 *
	 * @return the attribute value test
	 */
	@Test
	public void getAttributeValue(){
		arithmeticFilter = spy(new GTFilter("x", new Object()));
		int actualValue = (Integer) arithmeticFilter.getAttributeValue(new Runnable() {
			@SuppressWarnings("unused")
			int x = 10;
			public void run() {}
		});
		assertEquals(10, actualValue);
	}

}
