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
* Date   : Oct 26, 2013
*/
package com.cetsoft.imcache.cache.search.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Class MultiValueIndex is type of index where indexed value 
 * can have one or more corresponding values.
 */
public abstract class MultiValueIndex extends CacheIndexBase {

	/** The lock. */
	private Lock lock = new ReentrantLock();

	/** The map. */
	protected Map<Object, Set<Object>> map;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public void put(Object indexedKey, Object key) {
		Set<Object> keyList = map.get(indexedKey);
		if (keyList == null) {
			lock.lock();
			try {
				keyList = map.get(indexedKey);
				if (keyList == null) {
					keyList = new HashSet<Object>(3);
					map.put(indexedKey, keyList);
				}
			} finally {
				lock.unlock();
			}
		}
		synchronized (keyList) {
			keyList.add(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#remove(java.lang.Object
	 * , java.lang.Object)
	 */
	public void remove(Object indexedKey, Object key) {
		Set<Object> keyList = map.get(indexedKey);
		if (keyList == null) {
			return;
		}
		synchronized (keyList) {
			keyList.remove(key);
			if (keyList.size() == 0) {
				lock.lock();
				try {
					map.remove(key);
				} finally {
					lock.unlock();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#equalsTo(java.lang.
	 * Object)
	 */
	public List<Object> equalsTo(Object expectedValue) {
		Set<Object> result = map.get(expectedValue);
		if (result != null) {
			synchronized (result) {
				return new ArrayList<Object>(result);
			}
		}
		return Collections.emptyList();
	}
}
