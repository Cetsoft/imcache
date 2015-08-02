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

import java.util.concurrent.ConcurrentHashMap;

import com.cetsoft.imcache.cache.search.index.CacheIndex;

/**
 * The Class ConcurrentIndexHandler handles the indexes for the caches
 * in a thread-safe manner.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ConcurrentIndexHandler<K, V> extends DefaultIndexHandler<K, V> {

	/**
	 * Instantiates a new concurrent query executer.
	 */
	public ConcurrentIndexHandler() {
		indexes = new ConcurrentHashMap<String, CacheIndex>();
	}

}
