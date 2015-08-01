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
* Date   : May 20, 2014
*/
package com.cetsoft.imcache.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cetsoft.imcache.cache.search.filter.Filter;

/**
 * The Class CollectionItem is a list of CacheItem.
 *
 * @param <V> the value type
 */
public class CollectionItem<V> implements CacheItem<Collection<V>> {

	/** The objects. */
	private List<? extends V> objects;

	/**
	 * Instantiates a new collection item.
	 *
	 * @param objects the objects
	 */
	public CollectionItem(Collection<? extends V> objects) {
		this.objects = new ArrayList<V>(objects);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.CacheItem#getValue()
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> getValue() {
		return (Collection<V>) objects;
	}

	/**
	 * Filter.
	 *
	 * @param filter the filter
	 * @return the collection
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> filter(Filter filter) {
		List<Object> filteredObjects = filter.filter((List<Object>) objects);
		return (Collection<V>) filteredObjects;
	}

}
