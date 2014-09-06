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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.cache.coordinator;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheType;

/**
 * The Interface CacheCoordinator provides coordination of the various
 * Cache types. Each type of the cache is stored in CacheCoordinator and
 * can be retrieved from it whenever it is needed.
 */
public interface CacheCoordinator {
	
	/**
	 * Gets the cache associated with type.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param type the type
	 * @return the cache
	 */
	<K, V> Cache<K, V> getCache(CacheType<K, V> type);
	
	/**
	 * Adds the cache associated with type.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param type the type
	 * @param cache the cache
	 */
	<K, V> void addCache(CacheType<K, V> type, Cache<K, V> cache);
}
