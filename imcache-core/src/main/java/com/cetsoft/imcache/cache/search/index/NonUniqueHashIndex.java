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
* Date   : Sep 29, 2013
*/
package com.cetsoft.imcache.cache.search.index;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class NonUniqueHashIndex is type of index where hash indexed value 
 * can have one or more corresponding values.
 */
public class NonUniqueHashIndex extends MultiValueIndex {

	/**
	 * Instantiates a new non unique hash index.
	 */
	public NonUniqueHashIndex() {
		this.map = new ConcurrentHashMap<Object, Set<Object>>();
	}
}
