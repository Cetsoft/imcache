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
* Date   : Sep 26, 2013
*/
package com.cetsoft.imcache.cache;

/**
 * The Class SimpleItem is a basic implementation of a 
 * versionedItem.
 *
 * @param <V> the value type
 */
public class SimpleItem<V> implements VersionedItem<V>{

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

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.CacheItem#getValue()
	 */
	public V getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.VersionedCacheItem#getVersion()
	 */
	public int getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.VersionedCacheItem#setVersion(int)
	 */
	public int setVersion(int version) {
		this.version = version;
		return version;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.VersionedItem#update(java.lang.Object)
	 */
	public void update(V value) {
		this.value = value;
	}
	
}
