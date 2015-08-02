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
* Date   : Sep 15, 2013
*/
package com.cetsoft.imcache.cache;

/**
 * The Interface Cache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface Cache<K, V> {

	/**
	 * Puts the value with the specified key.
	 *
	 * @param key the key
	 * @param value the value
	 */
	void put(K key, V value);

	/**
	 * Gets the value with the specified key.
	 *
	 * @param key the key
	 * @return the value
	 */
	V get(K key);

	/**
	 * Invalidate the value with the specified key.
	 *
	 * @param key the key
	 * @return the value
	 */
	V invalidate(K key);

	/**
	 * Check if Cache contains the specified key.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	boolean contains(K key);

	/**
	 * Clear the cache.
	 */
	void clear();

	/**
	 * Calculates the Hit ratio.
	 *
	 * @return the double
	 */
	double hitRatio();

	/**
	 * Gets the specified name if exist, 
	 * otherwise returns the class name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	void setName(String name);
        
        /**
         * Returns the number of elements in this cache.
         * 
         * @return the number of elements
         */
        int size();
}
