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
 * Date   : May 20, 2014
 */
package com.cetsoft.imcache.cache.offheap;

/**
 * The Class StaleItemException is thrown where local cache item
 * does not have the latest version of the cache item.
 */
public class StaleItemException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5688389152095338751L;

	/**
	 * Instantiates a new stale item exception.
	 *
	 * @param expectedVersion the expected version
	 * @param actualVersion the actual version
	 */
	public StaleItemException(int expectedVersion, int actualVersion) {
		super("Expected version for the item is " + expectedVersion + " but the actual value was " + actualVersion);
	}

}
