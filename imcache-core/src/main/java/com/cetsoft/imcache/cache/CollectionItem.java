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
