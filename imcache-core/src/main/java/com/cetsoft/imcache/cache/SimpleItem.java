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
* Date   : Sep 26, 2013
*/
package com.cetsoft.imcache.cache;

/**
 * The Class SimpleItem is a basic implementation of a 
 * versionedItem.
 *
 * @param <V> the value type
 */
public class SimpleItem<V> implements VersionedItem<V> {

	/** The version. */
	private int version;

	/** The value. */
	private V value;

	/**
	 * Instantiates a new simple cache item.
	 *
	 * @param value the value
	 */
	public SimpleItem(V value) {
		this.value = value;
	}

	/**
	 * Instantiates a new simple cache item.
	 *
	 * @param version the version
	 * @param value the value
	 */
	public SimpleItem(int version, V value) {
		this.version = version;
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.CacheItem#getValue()
	 */
	public V getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.VersionedCacheItem#getVersion()
	 */
	public int getVersion() {
		return version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.VersionedCacheItem#setVersion(int)
	 */
	public int setVersion(int version) {
		this.version = version;
		return version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.VersionedItem#update(java.lang.Object)
	 */
	public void update(V value) {
		this.value = value;
	}

}
