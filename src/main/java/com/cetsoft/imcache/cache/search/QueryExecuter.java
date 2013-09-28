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
* Date   : Sep 28, 2013
*/
package com.cetsoft.imcache.cache.search;

import java.util.List;

/**
 * The Interface QueryExecuter is for executing queries as
 * well as adding and removing data while making sure that
 * they provide certain indexes.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface QueryExecuter<K,V> extends Indexable{

	/**
	 * Adds the key and value to be indexed.
	 *
	 * @param key the key
	 * @param value the value
	 */
	void add(K key, V value);

	/**
	 * Removes the key and value to be indexed.
	 *
	 * @param key the key
	 * @param value the value
	 */
	void remove(K key, V value);

	/**
	 * Clears the all the caches.
	 */
	void clear();
	
	/**
	 * Executes the given query.
	 *
	 * @param query the query
	 * @return the list
	 */
	List<K> execute(Query query);

}
