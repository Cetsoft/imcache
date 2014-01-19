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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.cache.builder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.search.ConcurrentIndexHandler;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class CacheBuilder.
 */
public abstract class CacheBuilder{
	
	/** The is searchable. */
	protected boolean isSearchable = false; 
	
	/** The Constant CACHE_LOADER. */
	protected static final CacheLoader<Object, Object> CACHE_LOADER = new CacheLoader<Object, Object>() {
		public Object load(Object key) {return null;}
	}; 
	
	/** The Constant EVICTION_LISTENER. */
	protected static final EvictionListener<Object, Object> EVICTION_LISTENER = new EvictionListener<Object, Object>() {
		public void onEviction(Object key, Object value) {}
	}; 
	
	/** The Constant QUERY_EXECUTER. */
	protected static final IndexHandler<Object, Object> QUERY_EXECUTER = new IndexHandler<Object, Object>() {
		public void addIndex(String attributeName, IndexType type) {}
		public void remove(Object key, Object value) {}
		public List<Object> execute(Query query) {return null;}
		public void clear() {}
		public void add(Object key, Object value) {}
	};
	
	/** The Constant SERIALIZER. */
	protected static final Serializer<Object> SERIALIZER = new Serializer<Object>() {
		public byte[] serialize(Object object){
			byte[] objectBytes = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			try {
				ObjectOutput out = new ObjectOutputStream(bos);  
				out.writeObject(object);
				objectBytes = bos.toByteArray();
				out.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return objectBytes;
		}
		public Object deserialize(byte [] bytes){
			Object object = null;
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			try {
				ObjectInput in = new ObjectInputStream(bis);
				object = in.readObject(); 
				bis.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch(ClassNotFoundException e){
				e.printStackTrace();
			}
			return object;
		}
	};
	
	/**
	 * Builds the cache.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return the searchable cache
	 */
	public abstract <K,V> SearchableCache<K, V> build();
	
	/**
	 * Builds the cache.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param cacheName the cache name
	 * @return the searchable cache
	 */
	public <K,V> SearchableCache<K, V> build(String cacheName){
		SearchableCache<K, V> cache = build();
		cache.setName(cacheName);
		return cache;
	}
	
	/** The cache loader. */
	protected CacheLoader<Object, Object> cacheLoader;
	
	/** The eviction listener. */
	protected EvictionListener<Object, Object> evictionListener;
	
	/** The query executer. */
	protected IndexHandler<Object, Object> indexHandler;
	
	/**
	 * Searchable.
	 */
	protected void searchable(){
		if(isSearchable){
			indexHandler = new ConcurrentIndexHandler<Object, Object>();
		}
	}

	/**
	 * Instantiates a new cache builder.
	 */
	protected CacheBuilder(){
		cacheLoader = CACHE_LOADER;
		evictionListener = EVICTION_LISTENER;
		indexHandler = QUERY_EXECUTER;
	}
	
	/**
	 * Heap cache.
	 *
	 * @return the heap cache builder
	 */
	public static HeapCacheBuilder heapCache(){
		return new HeapCacheBuilder();
	}
	
	/**
	 * Transactional Heap cache.
	 *
	 * @return the transactional heap cache builder
	 */
	public static TransactionalHeapCacheBuilder transactionalHeapCache(){
		return new TransactionalHeapCacheBuilder();
	}
	
	/**
	 * Concurrent heap cache.
	 *
	 * @return the concurrent heap cache builder
	 */
	public static ConcurrentHeapCacheBuilder concurrentHeapCache(){
		return new ConcurrentHeapCacheBuilder();
	}
	
	/**
	 * Off heap cache.
	 *
	 * @return the off heap cache builder
	 */
	public static OffHeapCacheBuilder offHeapCache(){
		return new OffHeapCacheBuilder();
	}
	
	/**
	 * Versioned Off heap cache.
	 *
	 * @return the off heap cache builder
	 */
	public static VersionedOffHeapCacheBuilder versionedOffHeapCache(){
		return new VersionedOffHeapCacheBuilder();
	}
	
}
