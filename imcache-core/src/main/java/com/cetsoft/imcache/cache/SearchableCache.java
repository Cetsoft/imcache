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
 * Date   : Sep 28, 2013
 */
package com.cetsoft.imcache.cache;

import java.util.List;

import com.cetsoft.imcache.cache.search.Query;

/**
 * The Interface SearchableCache provides query execution
 * for the caches.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public interface SearchableCache<K, V> extends Cache<K, V> {

	/**
	 * Gets the list of items as a result of query execution.
	 *
	 * @param query the query
	 * @return the list
	 */
	List<V> execute(Query query);
}
