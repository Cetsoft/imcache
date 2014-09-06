/*
 * Copyright (C) 2013 Yusuf Aytas, http://www.yusufaytas.com
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
 * Date   : Jan 6, 2014
 */
package com.cetsoft.imcache.cache.builder;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.search.DefaultIndexHandler;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;

/**
 * The Class TransactionalHeapCacheBuilder.
 */
public class TransactionalHeapCacheBuilder extends CacheBuilder {

	/** The capacity. */
	private int capacity = 10000;

	/** The transaction committer. */
	private TransactionCommitter<Object, Object> transactionCommitter;

	/**
	 * Instantiates a new transactional heap cache builder.
	 */
	public TransactionalHeapCacheBuilder() {
		super();
	}

	/**
	 * Transaction committer.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param transactionCommitter the transaction committer
	 * @return the transactional heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> TransactionalHeapCacheBuilder transactionCommitter(TransactionCommitter<K, V> transactionCommitter) {
		this.transactionCommitter = (TransactionCommitter<Object, Object>) transactionCommitter;
		return this;
	}

	/**
	 * Cache loader.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param cacheLoader the cache loader
	 * @return the transactional heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> TransactionalHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader) {
		this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
		return this;
	}

	/**
	 * Eviction listener.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param evictionListener the eviction listener
	 * @return the transactional heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> TransactionalHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener) {
		this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
		return this;
	}

	/**
	 * Query executer.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param indexHandler the query executer
	 * @return the transactional heap cache builder
	 */
	@SuppressWarnings("unchecked")
	public <K, V> TransactionalHeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler) {
		this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
		isSearchable = true;
		return this;
	}

	/**
	 * Capacity.
	 *
	 * @param capacity the capacity
	 * @return the transactional heap cache builder
	 */
	public TransactionalHeapCacheBuilder capacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	/**
	 * Adds the index.
	 *
	 * @param attributeName the attribute name
	 * @param indexType the index type
	 * @return the transactional heap cache builder
	 */
	public TransactionalHeapCacheBuilder addIndex(String attributeName, IndexType indexType) {
		searchable();
		indexHandler.addIndex(attributeName, indexType);
		return this;
	}

	/**
	 * Builds the cache.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return the cache
	 */
	@SuppressWarnings("unchecked")
	public <K, V> SearchableCache<K, V> build() {
		if (this.transactionCommitter == null) {
			throw new NecessaryArgumentException("TransactionCommitter must be set!");
		}
		return new TransactionalHeapCache<K, V>((TransactionCommitter<K, V>) transactionCommitter,
				(CacheLoader<K, V>) cacheLoader, (EvictionListener<K, V>) evictionListener,
				(IndexHandler<K, V>) indexHandler, capacity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.builder.CacheBuilder#searchable()
	 */
	@Override
	public void searchable() {
		if (!isSearchable) {
			indexHandler = new DefaultIndexHandler<Object, Object>();
			isSearchable = true;
		}
	}

}
