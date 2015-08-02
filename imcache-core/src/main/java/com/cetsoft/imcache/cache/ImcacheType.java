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
* Date   : May 20, 2014
*/
package com.cetsoft.imcache.cache;

/**
 * The Class ImcacheType is a class to specify cache types.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ImcacheType<K, V> implements CacheType<K, V> {

	/** The ordinal counter. */
	protected static volatile int ordinalCounter = 0;

	/** The ordinal. */
	private final int ordinal;

	/**
	 * Instantiates a new imcache type.
	 */
	public ImcacheType() {
		ordinal = ordinalCounter++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.CacheType#getType()
	 */
	public int getType() {
		return ordinal;
	}

}
