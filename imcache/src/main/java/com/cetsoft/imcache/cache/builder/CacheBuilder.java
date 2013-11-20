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

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.heap.ConcurrentHeapCache;
import com.cetsoft.imcache.cache.heap.HeapCache;
import com.cetsoft.imcache.cache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.offheap.OffHeapCache;
import com.cetsoft.imcache.cache.offheap.VersionedOffHeapCache;
import com.cetsoft.imcache.cache.search.ConcurrentIndexHandler;
import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.serialization.Serializer;


/**
 * The Class CacheBuilder.
 */
public abstract class CacheBuilder{
	
	/** The is searchable. */
	private boolean isSearchable = false; 
	
	/** The Constant CACHE_LOADER. */
	private static final CacheLoader<Object, Object> CACHE_LOADER = new CacheLoader<Object, Object>() {
		public Object load(Object key) {return null;}
	}; 
	
	/** The Constant EVICTION_LISTENER. */
	private static final EvictionListener<Object, Object> EVICTION_LISTENER = new EvictionListener<Object, Object>() {
		public void onEviction(Object key, Object value) {}
	}; 
	
	/** The Constant QUERY_EXECUTER. */
	private static final IndexHandler<Object, Object> QUERY_EXECUTER = new IndexHandler<Object, Object>() {
		public void addIndex(String attributeName, IndexType type) {}
		public void remove(Object key, Object value) {}
		public List<Object> execute(Query query) {return null;}
		public void clear() {}
		public void add(Object key, Object value) {}
	};
	
	private static final Serializer<Object> SERIALIZER = new Serializer<Object>() {
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
		if(!isSearchable){
			indexHandler = new ConcurrentIndexHandler<Object, Object>();
		}
	}

	/**
	 * Instantiates a new cache builder.
	 */
	private CacheBuilder(){
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
	
	/**
	 * The Class HeapCacheBuilder.
	 */
	public static class HeapCacheBuilder extends CacheBuilder{
		
		/** The capacity. */
		private int capacity = 10000;
		
		/**
		 * Instantiates a new heap cache builder.
		 */
		public HeapCacheBuilder(){	
			super();
		}
		
		/**
		 * Cache loader.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param cacheLoader the cache loader
		 * @return the heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> HeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		/**
		 * Eviction listener.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param evictionListener the eviction listener
		 * @return the heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> HeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		/**
		 * Query executer.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param indexHandler the query executer
		 * @return the heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> HeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler){
			this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
			return this;
		}
		
		/**
		 * Capacity.
		 *
		 * @param capacity the capacity
		 * @return the heap cache builder
		 */
		public HeapCacheBuilder capacity(int capacity){
			this.capacity = capacity;
			return this;
		}
		
		/**
		 * Adds the index.
		 *
		 * @param attributeName the attribute name
		 * @param indexType the index type
		 * @return the heap cache builder
		 */
		public HeapCacheBuilder addIndex(String attributeName, IndexType indexType){
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
		public <K,V> SearchableCache<K, V> build() {
			return new HeapCache<K, V>((CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener,
					(IndexHandler<K, V>) indexHandler, capacity);
		}
	}
	
	/**
	 * The Class ConcurrentHeapCacheBuilder.
	 */
	public static class ConcurrentHeapCacheBuilder extends CacheBuilder{
		
		/** The capacity. */
		private int capacity = 10000;
		
		/**
		 * Instantiates a new concurrent heap cache builder.
		 */
		public ConcurrentHeapCacheBuilder(){
			super();
		}
		
		/**
		 * Cache loader.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param cacheLoader the cache loader
		 * @return the concurrent heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> ConcurrentHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		/**
		 * Eviction listener.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param evictionListener the eviction listener
		 * @return the concurrent heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> ConcurrentHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		/**
		 * Query executer.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param indexHandler the query executer
		 * @return the concurrent heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> ConcurrentHeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler){
			this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
			return this;
		}
		
		/**
		 * Capacity.
		 *
		 * @param capacity the capacity
		 * @return the concurrent heap cache builder
		 */
		public ConcurrentHeapCacheBuilder capacity(int capacity){
			this.capacity = capacity;
			return this;
		}
		
		/**
		 * Adds the index.
		 *
		 * @param attributeName the attribute name
		 * @param indexType the index type
		 * @return the concurrent heap cache builder
		 */
		public ConcurrentHeapCacheBuilder addIndex(String attributeName, IndexType indexType){
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
		public <K,V> SearchableCache<K, V> build() {
			return new ConcurrentHeapCache<K, V>((CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener,
					(IndexHandler<K, V>) indexHandler, capacity);
		}
	}
	
	/**
	 * The Class OffHeapCacheBuilder.
	 */
	public static class OffHeapCacheBuilder extends CacheBuilder{
		
		/** The concurrency level. */
		int concurrencyLevel;
		
		/** The eviction period. */
		long evictionPeriod;
		
		/** The buffer cleaner period. */
		long bufferCleanerPeriod; 
		
		/** The serializer. */
		Serializer<Object> serializer;
		
		/** The buffer cleaner threshold. */
		float bufferCleanerThreshold; 
		
		/** The byte buffer store. */
		OffHeapByteBufferStore byteBufferStore;
		
		/**
		 * Instantiates a new off heap cache builder.
		 */
		public OffHeapCacheBuilder(){
			super();
			concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;
			evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;
			bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;
			bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;
			serializer = SERIALIZER;
		}
		
		/**
		 * Storage.
		 *
		 * @param bufferStore the buffer store
		 * @return the off heap cache builder
		 */
		public OffHeapCacheBuilder storage(OffHeapByteBufferStore bufferStore){
			this.byteBufferStore = bufferStore;
			return this;
		}
		
		/**
		 * Serializer.
		 *
		 * @param <V> the value type
		 * @param serializer the serializer
		 * @return the off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <V> OffHeapCacheBuilder serializer(Serializer<V> serializer){
			this.serializer = (Serializer<Object>) serializer;
			return this;
		}
		
		/**
		 * Cache loader.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param cacheLoader the cache loader
		 * @return the off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> OffHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		/**
		 * Eviction listener.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param evictionListener the eviction listener
		 * @return the off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> OffHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		/**
		 * Query executer.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param indexHandler the query executer
		 * @return the off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> OffHeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler){
			this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
			return this;
		}
		
		/**
		 * Concurrency level.
		 *
		 * @param concurrencyLevel the concurrency level
		 * @return the off heap cache builder
		 */
		public OffHeapCacheBuilder concurrencyLevel(int concurrencyLevel){
			if(concurrencyLevel>11&&concurrencyLevel<0){
				throw new IllegalArgumentException("ConcurrencyLevel must be between 0 and 11 inclusive.");
			}
			this.concurrencyLevel = concurrencyLevel;
			return this;
		}
		
		/**
		 * Eviction period.
		 *
		 * @param evictionPeriod the eviction period
		 * @return the off heap cache builder
		 */
		public OffHeapCacheBuilder evictionPeriod(long evictionPeriod){
			this.evictionPeriod = evictionPeriod;
			return this;
		}
		
		/**
		 * Buffer cleaner period.
		 *
		 * @param bufferCleanerPeriod the buffer cleaner period
		 * @return the off heap cache builder
		 */
		public OffHeapCacheBuilder bufferCleanerPeriod(long bufferCleanerPeriod){
			this.bufferCleanerPeriod = bufferCleanerPeriod;
			return this;
		}
		
		/**
		 * Buffer cleaner threshold.
		 *
		 * @param bufferCleanerThreshold the buffer cleaner threshold
		 * @return the off heap cache builder
		 */
		public OffHeapCacheBuilder bufferCleanerThreshold(float bufferCleanerThreshold){
			this.bufferCleanerThreshold = bufferCleanerThreshold;
			return this;
		}
		
		/**
		 * Adds the index.
		 *
		 * @param attributeName the attribute name
		 * @param indexType the index type
		 * @return the off heap cache builder
		 */
		public OffHeapCacheBuilder addIndex(String attributeName, IndexType indexType){
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
		public <K,V> Cache<K, V> build() {
			if(this.byteBufferStore==null){
				throw new NecessaryArgumentException("ByteBufferStore must be set!");
			}
			return new OffHeapCache<K, V>((CacheLoader<K, V>)cacheLoader, (EvictionListener<K, V>)evictionListener,(IndexHandler<K, V>)indexHandler,
					byteBufferStore, (Serializer<V>) serializer, bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
		}
	}
	
	/**
	 * The Class TransactionalHeapCacheBuilder.
	 */
	public static class TransactionalHeapCacheBuilder extends CacheBuilder{

		/** The capacity. */
		private int capacity = 10000;
		
		/** The transaction committer. */
		private TransactionCommitter<Object, Object> transactionCommitter;
		
		/**
		 * Instantiates a new transactional heap cache builder.
		 */
		public TransactionalHeapCacheBuilder(){
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
		public <K,V> TransactionalHeapCacheBuilder transactionCommitter(TransactionCommitter<K, V> transactionCommitter){
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
		public <K,V> TransactionalHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
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
		public <K,V> TransactionalHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
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
		public <K,V> TransactionalHeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler){
			this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
			return this;
		}
		
		/**
		 * Capacity.
		 *
		 * @param capacity the capacity
		 * @return the transactional heap cache builder
		 */
		public TransactionalHeapCacheBuilder capacity(int capacity){
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
		public TransactionalHeapCacheBuilder addIndex(String attributeName, IndexType indexType){
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
			if(this.transactionCommitter==null){
				throw new NecessaryArgumentException("TransactionCommitter must be set!");
			}
			return new TransactionalHeapCache<K, V>((TransactionCommitter<K, V>)transactionCommitter,
					(CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener,(IndexHandler<K, V>)indexHandler, capacity);
		}
		
	}
	
	/**
	 * The Class VersionedOffHeapCacheBuilder.
	 */
	public static class VersionedOffHeapCacheBuilder extends CacheBuilder{
		
		/** The concurrency level. */
		int concurrencyLevel;
		
		/** The eviction period. */
		long evictionPeriod;
		
		/** The buffer cleaner period. */
		long bufferCleanerPeriod; 
		
		/** The serializer. */
		Serializer<Object> serializer;
		
		/** The buffer cleaner threshold. */
		float bufferCleanerThreshold; 
		
		/** The byte buffer store. */
		OffHeapByteBufferStore byteBufferStore;
		
		/**
		 * Instantiates a new off heap cache builder.
		 */
		public VersionedOffHeapCacheBuilder(){
			super();
			concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;
			evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;
			bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;
			bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;
			serializer = SERIALIZER;
		}
		
		/**
		 * Storage.
		 *
		 * @param bufferStore the buffer store
		 * @return the off heap cache builder
		 */
		public VersionedOffHeapCacheBuilder storage(OffHeapByteBufferStore bufferStore){
			this.byteBufferStore = bufferStore;
			return this;
		}
		
		/**
		 * Serializer.
		 *
		 * @param <V> the value type
		 * @param serializer the serializer
		 * @return the off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <V> VersionedOffHeapCacheBuilder serializer(Serializer<V> serializer){
			this.serializer = (Serializer<Object>) serializer;
			return this;
		}
		
		/**
		 * Cache loader.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param cacheLoader the cache loader
		 * @return the off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> VersionedOffHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		/**
		 * Eviction listener.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param evictionListener the eviction listener
		 * @return the off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> VersionedOffHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		/**
		 * Query executer.
		 *
		 * @param <K> the key type
		 * @param <V> the value type
		 * @param indexHandler the query executer
		 * @return the versioned off heap cache builder
		 */
		@SuppressWarnings("unchecked")
		public <K,V> VersionedOffHeapCacheBuilder indexHandler(IndexHandler<K, V> indexHandler){
			this.indexHandler = (IndexHandler<Object, Object>) indexHandler;
			return this;
		}
		
		/**
		 * Concurrency level.
		 *
		 * @param concurrencyLevel the concurrency level
		 * @return the off heap cache builder
		 */
		public VersionedOffHeapCacheBuilder concurrencyLevel(int concurrencyLevel){
			if(concurrencyLevel>11&&concurrencyLevel<0){
				throw new IllegalArgumentException("ConcurrencyLevel must be between 0 and 11 inclusive.");
			}
			this.concurrencyLevel = concurrencyLevel;
			return this;
		}
		
		/**
		 * Eviction period.
		 *
		 * @param evictionPeriod the eviction period
		 * @return the off heap cache builder
		 */
		public VersionedOffHeapCacheBuilder evictionPeriod(long evictionPeriod){
			this.evictionPeriod = evictionPeriod;
			return this;
		}
		
		/**
		 * Buffer cleaner period.
		 *
		 * @param bufferCleanerPeriod the buffer cleaner period
		 * @return the off heap cache builder
		 */
		public VersionedOffHeapCacheBuilder bufferCleanerPeriod(long bufferCleanerPeriod){
			this.bufferCleanerPeriod = bufferCleanerPeriod;
			return this;
		}
		
		/**
		 * Buffer cleaner threshold.
		 *
		 * @param bufferCleanerThreshold the buffer cleaner threshold
		 * @return the off heap cache builder
		 */
		public VersionedOffHeapCacheBuilder bufferCleanerThreshold(float bufferCleanerThreshold){
			this.bufferCleanerThreshold = bufferCleanerThreshold;
			return this;
		}
		
		/**
		 * Adds the index.
		 *
		 * @param attributeName the attribute name
		 * @param indexType the index type
		 * @return the versioned off heap cache builder
		 */
		public VersionedOffHeapCacheBuilder addIndex(String attributeName, IndexType indexType){
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
		public <K,V> SearchableCache<K, VersionedItem<V>> build() {
			if(this.byteBufferStore==null){
				throw new NecessaryArgumentException("ByteBufferStore must be set!");
			}
			return new VersionedOffHeapCache<K, V>(byteBufferStore,(Serializer<V>)serializer, (CacheLoader<K, V>)cacheLoader,
					(EvictionListener<K, V>)evictionListener, (IndexHandler<K, V>)indexHandler,bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
		}
	}
	
	/**
	 * The Class NecessaryArgumentException.
	 */
	public static class NecessaryArgumentException extends RuntimeException{

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -7420658148641484150L;

		/**
		 * Instantiates a new necessary argument exception.
		 *
		 * @param exception the exception
		 */
		public NecessaryArgumentException(String exception){
			super(exception);
		}
	}
}
