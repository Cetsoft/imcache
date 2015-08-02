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
* Date   : Oct 26, 2013
*/
package com.cetsoft.imcache.cache.search.index;

import java.util.List;

/**
 * The Class CacheIndexBase throws UnsupportedOperationException for all methods to be
 * implemented.
 */
public abstract class CacheIndexBase implements CacheIndex {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#equalsTo(java.lang.
	 * Object)
	 */
	public List<Object> equalsTo(Object expectedValue) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#lessThan(java.lang.
	 * Object)
	 */
	public List<Object> lessThan(Object value) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#lessThanOrEqualsTo(
	 * java.lang.Object)
	 */
	public List<Object> lessThanOrEqualsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#greaterThan(java.lang
	 * .Object)
	 */
	public List<Object> greaterThan(Object value) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#greaterThanOrEqualsTo
	 * (java.lang.Object)
	 */
	public List<Object> greaterThanOrEqualsTo(Object value) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.search.index.CacheIndex#between(java.lang.Object
	 * , java.lang.Object)
	 */
	public List<Object> between(Object lowerBound, Object upperBound) {
		throw new UnsupportedOperationException();
	}

}
