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
* Date   : Sep 28, 2013
*/
package com.cetsoft.imcache.cache.search;

import java.util.List;

/**
 * The Interface IndexHandler is for executing queries as
 * well as adding and removing data while making sure that
 * they provide certain indexes.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface IndexHandler<K, V> extends Indexable {

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
