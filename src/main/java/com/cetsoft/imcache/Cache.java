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
* Date   : Sep 15, 2013
*/
package com.cetsoft.imcache;

/**
 * The Interface Cache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface Cache<K, V> {

	/**
	 * Put.
	 *
	 * @param key the key
	 * @param value the value
	 */
	void put(K key, V value);

	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the value
	 */
	V get(K key);

	/**
	 * Invalidate.
	 *
	 * @param key the key
	 * @return the value
	 */
	V invalidate(K key);
	
	/**
	 * Check if Cache Contains the specified key.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	boolean contains(K key);

	/**
	 * Clear the cache.
	 */
	void clear();
}
