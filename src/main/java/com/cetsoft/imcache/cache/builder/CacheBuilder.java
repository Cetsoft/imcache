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

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.ConcurrentHeapCache;
import com.cetsoft.imcache.cache.heap.HeapCache;
import com.cetsoft.imcache.cache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.offheap.OffHeapCache;
import com.cetsoft.imcache.serialization.Serializer;


/**
 * The Class CacheBuilder.
 */
public abstract class CacheBuilder{
	
	/** The Constant CACHE_LOADER. */
	private static final CacheLoader<Object, Object> CACHE_LOADER = new CacheLoader<Object, Object>() {
		public Object load(Object key) {return null;}
	}; 
	
	/** The Constant EVICTION_LISTENER. */
	private static final EvictionListener<Object, Object> EVICTION_LISTENER = new EvictionListener<Object, Object>() {
		public void onEviction(Object key, Object value) {}
	}; 
	
	/** The cache loader. */
	protected CacheLoader<Object, Object> cacheLoader;
	
	/** The eviction listener. */
	protected EvictionListener<Object, Object> evictionListener;

	/**
	 * Instantiates a new cache builder.
	 */
	private CacheBuilder(){}
	
	/**
	 * Builds the.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return the cache
	 */
	public abstract <K,V> Cache<K,V> build();
	
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
	 * The Class HeapCacheBuilder.
	 */
	public static class HeapCacheBuilder extends CacheBuilder{
		
		/** The capacity. */
		private int capacity = 10000;
		
		/**
		 * Instantiates a new heap cache builder.
		 */
		public HeapCacheBuilder(){
			cacheLoader = CACHE_LOADER;
			evictionListener = EVICTION_LISTENER;
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
		 * Capacity.
		 *
		 * @param capacity the capacity
		 * @return the heap cache builder
		 */
		public HeapCacheBuilder capacity(int capacity){
			this.capacity = capacity;
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.builder.CacheBuilder#build()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public <K,V> Cache<K, V> build() {
			return new HeapCache<K, V>((CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener, capacity);
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
			cacheLoader = CACHE_LOADER;
			evictionListener = EVICTION_LISTENER;
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
		 * Capacity.
		 *
		 * @param capacity the capacity
		 * @return the concurrent heap cache builder
		 */
		public ConcurrentHeapCacheBuilder capacity(int capacity){
			this.capacity = capacity;
			return this;
		}
		
		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.builder.CacheBuilder#build()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public <K,V> Cache<K, V> build() {
			return new ConcurrentHeapCache<K, V>((CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener, capacity);
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
			cacheLoader = CACHE_LOADER;
			evictionListener = EVICTION_LISTENER;
			concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;
			evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;
			bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;
			bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;
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
		
		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.builder.CacheBuilder#build()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public <K,V> Cache<K, V> build() {
			if(this.byteBufferStore==null){
				throw new NecessaryArgumentException("ByteBufferStore must be set!");
			}
			if(this.serializer==null){
				throw new NecessaryArgumentException("Serializer must be set!");
			}
			return new OffHeapCache<K, V>((CacheLoader<K, V>)cacheLoader, (EvictionListener<K, V>)evictionListener, byteBufferStore, 
					(Serializer<V>) serializer, bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
		}
	}
	
	public static class TransactionalHeapCacheBuilder extends CacheBuilder{

		/** The capacity. */
		private int capacity = 10000;
		private TransactionCommitter<Object, Object> transactionCommitter;
		
		public TransactionalHeapCacheBuilder(){
			cacheLoader = CACHE_LOADER;
			evictionListener = EVICTION_LISTENER;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> TransactionalHeapCacheBuilder transactionCommitter(TransactionCommitter<K, V> transactionCommitter){
			this.transactionCommitter = (TransactionCommitter<Object, Object>) transactionCommitter;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> TransactionalHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> TransactionalHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		public TransactionalHeapCacheBuilder capacity(int capacity){
			this.capacity = capacity;
			return this;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <K, V> Cache<K, V> build() {
			if(this.transactionCommitter==null){
				throw new NecessaryArgumentException("TransactionCommitter must be set!");
			}
			return new TransactionalHeapCache<K, V>((TransactionCommitter<K, V>)transactionCommitter,
					(CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener, capacity);
		}
		
	}
	
	public static class NecessaryArgumentException extends RuntimeException{

		private static final long serialVersionUID = -7420658148641484150L;

		public NecessaryArgumentException(String exception){
			super(exception);
		}
	}
}
