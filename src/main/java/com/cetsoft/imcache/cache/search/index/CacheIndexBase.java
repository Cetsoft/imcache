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
* Date   : Oct 26, 2013
*/
package com.cetsoft.imcache.cache.search.index;

import java.util.List;

/**
 * The Class CacheIndexBase throws UnsupportedOperationException for all methods to be
 * implemented.
 */
public abstract class CacheIndexBase implements CacheIndex{
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#equalsTo(java.lang.Object)
	 */
	public List<Object> equalsTo(Object expectedValue) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#lessThan(java.lang.Object)
	 */
	public List<Object> lessThan(Object value) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#lessThanOrEqualsTo(java.lang.Object)
	 */
	public List<Object> lessThanOrEqualsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#greaterThan(java.lang.Object)
	 */
	public List<Object> greaterThan(Object value) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#greaterThanOrEqualsTo(java.lang.Object)
	 */
	public List<Object> greaterThanOrEqualsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.index.CacheIndex#between(java.lang.Object, java.lang.Object)
	 */
	public List<Object> between(Object lowerBound, Object upperBound) {
		throw new UnsupportedOperationException();
	}

}
