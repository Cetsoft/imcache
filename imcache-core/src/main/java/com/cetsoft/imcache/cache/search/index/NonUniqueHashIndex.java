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
 * Date   : Sep 29, 2013
 */
package com.cetsoft.imcache.cache.search.index;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class NonUniqueHashIndex is type of index where hash indexed value 
 * can have one or more corresponding values.
 */
public class NonUniqueHashIndex extends MultiValueIndex {

	/**
	 * Instantiates a new non unique hash index.
	 */
	public NonUniqueHashIndex() {
		this.map = new ConcurrentHashMap<Object, Set<Object>>();
	}
}
