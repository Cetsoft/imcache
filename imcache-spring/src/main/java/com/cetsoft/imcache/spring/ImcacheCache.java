/*
* Copyright (C) 2014 Yusuf Aytas, http://www.yusufaytas.com
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
* Date   : Dec 29, 2013
*/
package com.cetsoft.imcache.spring;

import org.springframework.cache.support.SimpleValueWrapper;

import com.cetsoft.imcache.cache.Cache;

/**
 * The Class ImcacheCache.
 */
public class ImcacheCache implements org.springframework.cache.Cache {

	/** The cache. */
	private Cache<Object, Object> cache;

	/**
	 * Instantiates a new imcache cache.
	 *
	 * @param cache the cache
	 */
	@SuppressWarnings("unchecked")
	public ImcacheCache(Cache<?, ?> cache) {
		this.cache = (Cache<Object, Object>) cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.cache.Cache#getName()
	 */
	public String getName() {
		return cache.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.cache.Cache#getNativeCache()
	 */
	public Object getNativeCache() {
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.cache.Cache#get(java.lang.Object)
	 */
	public ValueWrapper get(Object key) {
		if (key == null) {
			return null;
		}
		final Object value = cache.get(key);
		return value != null ? new SimpleValueWrapper(value) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.cache.Cache#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public void put(Object key, Object value) {
		cache.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.cache.Cache#evict(java.lang.Object)
	 */
	public void evict(Object key) {
		cache.invalidate(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.cache.Cache#clear()
	 */
	public void clear() {
		cache.clear();
	}

	/* (non-Javadoc)
	 * @see org.springframework.cache.Cache#get(java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> clazz) {
		if (key == null) {
			return null;
		}
		return (T) cache.get(key);
	}

}
