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
* Date   : Jun 5, 2014
*/
package com.cetsoft.imcache.cache.async;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * The Class BasicEvictionListenerTest.
 */
public class BasicEvictionListenerTest {
	
	/** The async eviction listener. */
	BasicEvictionListener<Object, Object> asyncEvictionListener;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		asyncEvictionListener = spy( new BasicEvictionListener<Object, Object>() {
			void save(Object key, Object value) {}
		});
	}
	
	/**
	 * On eviction.
	 */
	@Test
	public void onEviction(){
		Object key = new Object(), value = new Object();
		doNothing().when(asyncEvictionListener).save(key, value);
		asyncEvictionListener.onEviction(key, value);
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {}
		verify(asyncEvictionListener).save(key, value);
	}

}
