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
