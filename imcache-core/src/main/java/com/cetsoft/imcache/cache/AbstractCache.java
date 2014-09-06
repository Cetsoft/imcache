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
* Date   : Sep 16, 2013
*/
package com.cetsoft.imcache.cache;

import java.util.ArrayList;
import java.util.List;

import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.IndexHandler;

/**
 * The Class AbstractCache.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class AbstractCache<K, V> implements SearchableCache<K, V> {
	
	/** The name. */
	private String name;

	/** The cache loader. */
	protected CacheLoader<K, V> cacheLoader;
	
	/** The eviction listener. */
	protected EvictionListener<K, V> evictionListener;
	
	/** The Query Executer.*/
	protected IndexHandler<K, V> indexHandler;
	
	/**
	 * Instantiates a new abstract cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the query executer
	 */
	public AbstractCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener,IndexHandler<K, V> indexHandler) {
		this.cacheLoader = cacheLoader;
		this.evictionListener = evictionListener;
		this.indexHandler = indexHandler;
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.SearchableCache#get(com.cetsoft.imcache.cache.search.Query)
	 */
	@SuppressWarnings("unchecked")
	public List<V> execute(Query query){
		List<K> keys = indexHandler.execute(query);
		List<V> values = new ArrayList<V>(keys.size());
		for (K key : keys) {
			V value = get(key);
			if(value!=null){
				values.add(value);
			}
		}
		if(query.getFilter()!=null){
			values = (List<V>) query.getFilter().filter((List<Object>) values);
		}
		return values;
	}

	/**
	 * Gets the cache loader.
	 *
	 * @return the cache loader
	 */
	public CacheLoader<K, V> getCacheLoader() {
		return cacheLoader;
	}

	/**
	 * Sets the cache loader.
	 *
	 * @param cacheLoader the cache loader
	 */
	public void setCacheLoader(CacheLoader<K, V> cacheLoader) {
		this.cacheLoader = cacheLoader;
	}

	/**
	 * Gets the eviction listener.
	 *
	 * @return the eviction listener
	 */
	public EvictionListener<K, V> getEvictionListener() {
		return evictionListener;
	}

	/**
	 * Sets the eviction listener.
	 *
	 * @param evictionListener the eviction listener
	 */
	public void setEvictionListener(EvictionListener<K, V> evictionListener) {
		this.evictionListener = evictionListener;
	}

	/**
	 * Gets the query executer.
	 *
	 * @return the query executer
	 */
	public IndexHandler<K, V> getIndexHandler() {
		return indexHandler;
	}

	/**
	 * Sets the query executer.
	 *
	 * @param indexHandler the query executer
	 */
	public void setIndexHandler(IndexHandler<K, V> indexHandler) {
		this.indexHandler = indexHandler;
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#getName()
	 */
	public String getName(){
		if(name!=null){
			return name;
		}
		return this.getClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

}
