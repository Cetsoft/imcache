/*
 * Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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
