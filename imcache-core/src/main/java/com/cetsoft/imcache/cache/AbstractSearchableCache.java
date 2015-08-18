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
* Date   : Aug 18, 2015
*/
package com.cetsoft.imcache.cache;

import java.util.ArrayList;
import java.util.List;

import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.Query;

/**
 * The Class AbstractSearchableCache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class AbstractSearchableCache<K,V> extends AbstractCache<K, V> implements SearchableCache<K, V>{
	
	/** The index handler. */
	protected IndexHandler<K, V> indexHandler;
	
	/**
	 * Instantiates a new abstract searchable cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the index handler
	 */
	public AbstractSearchableCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener, 
			IndexHandler<K, V> indexHandler) {
		super(cacheLoader, evictionListener);
		this.indexHandler = indexHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.SearchableCache#get(com.cetsoft.imcache.cache
	 * .search.Query)
	 */
	@SuppressWarnings("unchecked")
	public List<V> execute(Query query) {
		List<K> keys = indexHandler.execute(query);
		List<V> values = new ArrayList<V>(keys.size());
		for (K key : keys) {
			V value = get(key);
			if (value != null) {
				values.add(value);
			}
		}
		if (query.getFilter() != null) {
			values = (List<V>) query.getFilter().filter((List<Object>) values);
		}
		return values;
	}
}
