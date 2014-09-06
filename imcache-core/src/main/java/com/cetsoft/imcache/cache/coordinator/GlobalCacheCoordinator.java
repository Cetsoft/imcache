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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.cache.coordinator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheType;

/**
 * The Class GlobalCacheCoordinator provides global coordination for 
 * caches. This coordinator can be used for concurrent accesses.
 */
public class GlobalCacheCoordinator implements CacheCoordinator{
	
	/** The lock. */
	private Lock lock = new ReentrantLock();
	
	/** The cache map. */
	@SuppressWarnings("rawtypes")
	ConcurrentMap<Integer, Cache> cacheMap = new ConcurrentHashMap<Integer, Cache>();

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.coordinator.CacheCoordinator#getCache(com.cetsoft.imcache.cache.CacheType)
	 */
	@SuppressWarnings("unchecked")
	public <K, V> Cache<K, V> getCache(CacheType<K, V> type) {
		return cacheMap.get(type.getType());
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.coordinator.CacheCoordinator#addCache(com.cetsoft.imcache.cache.CacheType, com.cetsoft.imcache.cache.Cache)
	 */
	public <K, V> void addCache(CacheType<K, V> type, Cache<K, V> cache) {
		if(getCache(type)==null){
			lock.lock();
			try{
				if(getCache(type)==null){
					cacheMap.put(type.getType(), cache);
				}
			}finally{
				lock.unlock();
			}
		}
	}

}
