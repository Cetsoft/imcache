package com.cetsoft.imcache.cache.builder;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.ConcurrentHeapCache;
import com.cetsoft.imcache.cache.heap.HeapCache;
import com.cetsoft.imcache.cache.offheap.OffHeapCache;
import com.cetsoft.imcache.serialization.Serializer;


public abstract class CacheBuilder{
	
	private static final CacheLoader<Object, Object> CACHE_LOADER = new CacheLoader<Object, Object>() {
		public Object load(Object key) {return null;}
	}; 
	
	private static final EvictionListener<Object, Object> EVICTION_LISTENER = new EvictionListener<Object, Object>() {
		public void onEviction(Object key, Object value) {}
	}; 
	
	protected CacheLoader<Object, Object> cacheLoader;
	protected EvictionListener<Object, Object> evictionListener;

	private CacheBuilder(){}
	
	public abstract <K,V> Cache<K,V> build();
	
	public static HeapCacheBuilder heapCache(){
		return new HeapCacheBuilder();
	}
	
	public static ConcurrentHeapCacheBuilder concurrentHeapCache(){
		return new ConcurrentHeapCacheBuilder();
	}
	
	public static OffHeapCacheBuilder offHeapCache(){
		return new OffHeapCacheBuilder();
	}
	
	public static class HeapCacheBuilder extends CacheBuilder{
		
		private int capacity = 10000;
		
		public HeapCacheBuilder(){
			cacheLoader = CACHE_LOADER;
			evictionListener = EVICTION_LISTENER;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> HeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> HeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		public HeapCacheBuilder capacity(int capacity){
			this.capacity = capacity;
			return this;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <K,V> Cache<K, V> build() {
			return new HeapCache<K, V>((CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener, capacity);
		}
	}
	
	public static class ConcurrentHeapCacheBuilder extends CacheBuilder{
		
		private int capacity = 10000;
		
		public ConcurrentHeapCacheBuilder(){
			cacheLoader = CACHE_LOADER;
			evictionListener = EVICTION_LISTENER;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> ConcurrentHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> ConcurrentHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		public ConcurrentHeapCacheBuilder capacity(int capacity){
			this.capacity = capacity;
			return this;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <K,V> Cache<K, V> build() {
			return new ConcurrentHeapCache<K, V>((CacheLoader<K, V>)cacheLoader,(EvictionListener<K, V>) evictionListener, capacity);
		}
	}
	
	public static class OffHeapCacheBuilder extends CacheBuilder{
		
		int concurrencyLevel;
		long evictionPeriod;
		long bufferCleanerPeriod; 
		Serializer<Object> serializer;
		float bufferCleanerThreshold; 
		OffHeapByteBufferStore byteBufferStore;
		
		public OffHeapCacheBuilder(){
			cacheLoader = CACHE_LOADER;
			evictionListener = EVICTION_LISTENER;
			concurrencyLevel = OffHeapCache.DEFAULT_CONCURRENCY_LEVEL;
			evictionPeriod = OffHeapCache.DEFAULT_EVICTION_PERIOD;
			bufferCleanerPeriod = OffHeapCache.DEFAULT_BUFFER_CLEANER_PERIOD;
			bufferCleanerThreshold = OffHeapCache.DEFAULT_BUFFER_CLEANER_THRESHOLD;
		}
		
		public OffHeapCacheBuilder storage(OffHeapByteBufferStore bufferStore){
			this.byteBufferStore = bufferStore;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <V> OffHeapCacheBuilder serializer(Serializer<V> serializer){
			this.serializer = (Serializer<Object>) serializer;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> OffHeapCacheBuilder cacheLoader(CacheLoader<K, V> cacheLoader){
			this.cacheLoader = (CacheLoader<Object, Object>) cacheLoader;
			return this;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> OffHeapCacheBuilder evictionListener(EvictionListener<K, V> evictionListener){
			this.evictionListener = (EvictionListener<Object, Object>) evictionListener;
			return this;
		}
		
		public OffHeapCacheBuilder concurrencyLevel(int concurrencyLevel){
			if(concurrencyLevel>11&&concurrencyLevel<0){
				throw new IllegalArgumentException("ConcurrencyLevel must be between 0 and 11 inclusive.");
			}
			this.concurrencyLevel = concurrencyLevel;
			return this;
		}
		
		public OffHeapCacheBuilder evictionPeriod(long evictionPeriod){
			this.evictionPeriod = evictionPeriod;
			return this;
		}
		
		public OffHeapCacheBuilder bufferCleanerPeriod(long bufferCleanerPeriod){
			this.bufferCleanerPeriod = bufferCleanerPeriod;
			return this;
		}
		
		public OffHeapCacheBuilder bufferCleanerThreshold(float bufferCleanerThreshold){
			this.bufferCleanerThreshold = bufferCleanerThreshold;
			return this;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <K,V> Cache<K, V> build() {
			if(this.byteBufferStore==null){
				throw new NullPointerException("ByteBufferStore must be set!");
			}
			if(this.serializer==null){
				throw new NullPointerException("Serializer must be set!");
			}
			return new OffHeapCache<K, V>((CacheLoader<K, V>)cacheLoader, (EvictionListener<K, V>)evictionListener, byteBufferStore, 
					(Serializer<V>) serializer, bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
		}
	}
}
