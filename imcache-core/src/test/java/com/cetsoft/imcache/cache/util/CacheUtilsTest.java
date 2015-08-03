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
* Date   : Aug 3, 2015
*/
package com.cetsoft.imcache.cache.util;

import org.junit.Test;

import com.cetsoft.imcache.cache.CacheEntry;

public class CacheUtilsTest {

	@Test
	public void createEntry(){
		CacheEntry<Integer, Integer>  cacheEntry = CacheUtils.createEntry(1, 2);
		assert(1 == cacheEntry.getKey());
		assert(2 == cacheEntry.getValue());
	}
}
