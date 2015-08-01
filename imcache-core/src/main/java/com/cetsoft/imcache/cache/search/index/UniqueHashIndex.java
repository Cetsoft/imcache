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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class UniqueHashIndex is type of index where indexed value 
 * can have exactly one  corresponding value.
 */
public class UniqueHashIndex extends CacheIndexBase {

	/** The map. */
	protected Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public void put(Object indexedKey, Object key) {
		map.put(indexedKey, key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#remove(java.lang.Object
	 * , java.lang.Object)
	 */
	public void remove(Object indexedKey, Object key) {
		map.remove(indexedKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#equalsTo(java.lang.
	 * Object)
	 */
	public List<Object> equalsTo(Object expectedValue) {
		List<Object> keys = new ArrayList<Object>(1);
		Object indexedKey = map.get(expectedValue);
		keys.add(indexedKey);
		return keys;
	}

}
