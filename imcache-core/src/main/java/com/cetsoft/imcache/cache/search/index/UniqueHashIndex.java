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
public class UniqueHashIndex extends CacheIndexBase{
	
	/** The map. */
	protected Map<Object,Object> map = new ConcurrentHashMap<Object, Object>();

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#put(java.lang.Object, java.lang.Object)
	 */
	public void put(Object indexedKey, Object key) {
		map.put(indexedKey, key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#remove(java.lang.Object, java.lang.Object)
	 */
	public void remove(Object indexedKey, Object key) {
		map.remove(indexedKey);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#equalsTo(java.lang.Object)
	 */
	public List<Object> equalsTo(Object expectedValue) {
		List<Object> keys = new ArrayList<Object>(1);
		Object indexedKey = map.get(expectedValue);
		keys.add(indexedKey);
		return keys;
	}

}
