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
* Date   : May 20, 2014
*/
package com.cetsoft.imcache.cache;

/**
 * The Class ImcacheType is a class to specify cache types.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ImcacheType<K,V> implements CacheType<K,V> {

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

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.CacheType#getType()
	 */
	public int getType() {
		return ordinal;
	}

}
