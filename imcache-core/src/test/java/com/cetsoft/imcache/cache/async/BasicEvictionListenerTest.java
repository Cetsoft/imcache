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
			Thread.sleep(10);
		} catch (InterruptedException e) {}
		verify(asyncEvictionListener).save(key, value);
	}

}
