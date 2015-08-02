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
* Date   : May 25, 2014
*/
package com.cetsoft.imcache.cache.search.index;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.search.index.CacheIndexBase;

/**
 * The Class CacheIndexBaseTest.
 */
public class CacheIndexBaseTest {

	/** The index base. */
	CacheIndexBase indexBase;
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		indexBase = spy(new CacheIndexBase() {
			public void remove(Object indexedKey, Object key) {}
			public void put(Object indexedKey, Object key) {}
		});
	}
	
	/**
	 * Equals to.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void equalsTo(){
		indexBase.equalsTo(new Object());
	}

	/**
	 * Less than.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void lessThan(){
		indexBase.lessThan(new Object());
	}
	
	/**
	 * Less than or equals to.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void lessThanOrEqualsTo(){
		indexBase.lessThanOrEqualsTo(new Object());
	}
	
	/**
	 * Greater than.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void greaterThan(){
		indexBase.greaterThan(new Object());
	}
	
	/**
	 * Greater than or equals to.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void greaterThanOrEqualsTo(){
		indexBase.greaterThanOrEqualsTo(new Object());
	}
	
	/**
	 * Between.
	 */
	@Test(expected=UnsupportedOperationException.class)
	public void between(){
		indexBase.between(new Object(), new Object());
	}
	
}
