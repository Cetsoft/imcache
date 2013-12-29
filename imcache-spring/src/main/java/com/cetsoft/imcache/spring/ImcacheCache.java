/*
* Copyright (C) 2013 Yusuf Aytas, http://www.yusufaytas.com
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
* Date   : Dec 29, 2013
*/
package com.cetsoft.imcache.spring;

import org.springframework.cache.support.SimpleValueWrapper;

import com.cetsoft.imcache.cache.Cache;

/**
 * The Class ImcacheCache.
 */
public class ImcacheCache implements org.springframework.cache.Cache{
	
	/** The cache. */
	private Cache<Object,Object> cache;
	
	/**
	 * Instantiates a new imcache cache.
	 *
	 * @param cache the cache
	 */
	@SuppressWarnings("unchecked")
	public ImcacheCache(Cache<?,?> cache){
		this.cache = (Cache<Object, Object>) cache;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#getName()
	 */
	public String getName() {
		return cache.getName();
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#getNativeCache()
	 */
	public Object getNativeCache() {
		return cache;
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#get(java.lang.Object)
	 */
	public ValueWrapper get(Object key) {
		if (key == null) {
            return null;
        }
        final Object value = cache.get(key);
        return value != null ? new SimpleValueWrapper(value) : null;
	}

    
	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#evict(java.lang.Object)
	 */
	public void evict(Object key) {
		cache.invalidate(key);
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#clear()
	 */
	public void clear() {
		cache.clear();
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> clazz) {
		if (key == null) {
            return null;
        }
		return (T) cache.get(key);
	}

}
