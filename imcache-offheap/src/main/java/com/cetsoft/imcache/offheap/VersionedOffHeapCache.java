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
* Date   : Sep 26, 2013
*/
package com.cetsoft.imcache.offheap;

import java.util.List;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.SimpleItem;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.cache.util.SerializationUtils;
import com.cetsoft.imcache.concurrent.lock.StripedReadWriteLock;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class VersionedOffHeapCache is a type of offheap cache where cache items
 * have versions that are incremented for each update.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class VersionedOffHeapCache<K, V> implements SearchableCache<K, VersionedItem<V>> {

	/** The name. */
	private String name;

	/** The off heap cache. */
	protected OffHeapCache<K, VersionedItem<V>> offHeapCache;
	
	/** The read write lock. */
	private StripedReadWriteLock readWriteLock;

	/**
	 * Instantiates a new versioned off heap cache.
	 *
	 * @param serializer the serializer
	 * @param byteBufferStore the byte buffer store
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the index handler
	 * @param bufferCleanerPeriod the buffer cleaner period
	 * @param bufferCleanerThreshold the buffer cleaner threshold
	 * @param concurrencyLevel the concurrency level
	 * @param evictionPeriod the eviction period
	 */
	public VersionedOffHeapCache(Serializer<VersionedItem<V>> serializer, OffHeapByteBufferStore byteBufferStore,
			CacheLoader<K, VersionedItem<V>> cacheLoader, EvictionListener<K, VersionedItem<V>> evictionListener,
			IndexHandler<K, VersionedItem<V>> indexHandler, long bufferCleanerPeriod, float bufferCleanerThreshold,
			int concurrencyLevel, long evictionPeriod) {
		offHeapCache = new OffHeapCache<K, VersionedItem<V>>(cacheLoader, evictionListener, indexHandler,
				byteBufferStore, serializer, bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel,
				evictionPeriod);
		this.readWriteLock = new StripedReadWriteLock(concurrencyLevel);
	}

	/**
	 * Instantiates a new versioned off heap cache.
	 *
	 * @param byteBufferStore the byte buffer store
	 * @param serializer the serializer
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the index handler
	 * @param bufferCleanerPeriod the buffer cleaner period
	 * @param bufferCleanerThreshold the buffer cleaner threshold
	 * @param concurrencyLevel the concurrency level
	 * @param evictionPeriod the eviction period
	 */
	public VersionedOffHeapCache(OffHeapByteBufferStore byteBufferStore, Serializer<V> serializer,
			CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener, IndexHandler<K, V> indexHandler,
			long bufferCleanerPeriod, float bufferCleanerThreshold, int concurrencyLevel, long evictionPeriod) {
		this(new CacheItemSerializer<V>(serializer), byteBufferStore, new CacheItemCacheLoader<K, V>(cacheLoader),
				new CacheItemEvictionListener<K, V>(evictionListener), new CacheItemIndexHandler<K, V>(indexHandler),
				bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public void put(K key, VersionedItem<V> value) {
		int version = value.getVersion();
		VersionedItem<V> exValue = get(key);
		if (exValue != null && version != exValue.getVersion()) {
			throw new StaleItemException(version, exValue.getVersion());
		}
		version++;
		writeLock(key);
		try{
			exValue = get(key);
			if (value.getVersion() == version) {
				throw new StaleItemException(version, exValue == null ? version - 1 : exValue.getVersion());
			}
			value.setVersion(version);
			offHeapCache.put(key, value);
		}finally{
			writeUnlock(key);
		}
	}
	
	/**
	 * Write Lock for key is locked.
	 * 
	 * @param key the key
	 */
	protected void writeLock(K key) {
		readWriteLock.writeLock(Math.abs(key.hashCode()));
	}

	/**
	 * Write Lock for key is unlocked.
	 * 
	 * @param key the key
	 */
	protected void writeUnlock(K key) {
		readWriteLock.writeUnlock(Math.abs(key.hashCode()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#get(java.lang.Object)
	 */
	public VersionedItem<V> get(K key) {
		return offHeapCache.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#invalidate(java.lang.Object)
	 */
	public VersionedItem<V> invalidate(K key) {
		return offHeapCache.invalidate(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(K key) {
		return offHeapCache.contains(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#clear()
	 */
	public void clear() {
		offHeapCache.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#hitRatio()
	 */
	public double hitRatio() {
		return offHeapCache.hitRatio();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#size()
	 */        
        public int size() {
                return offHeapCache.size();
        }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.SearchableCache#execute(com.cetsoft.imcache
	 * .cache.search.Query)
	 */
	public List<VersionedItem<V>> execute(Query query) {
		return offHeapCache.execute(query);
	}

	/**
	 * The listener interface for receiving cacheItemEviction events. The class
	 * that is interested in processing a cacheItemEviction event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the constructors. When the cacheItemEviction event
	 * occurs, that object's appropriate method is invoked.
	 * 
	 * @param <K> the key type
	 * @param <V> the value type
	 * @see CacheItemEvictionEvent
	 */
	private static class CacheItemEvictionListener<K, V> implements EvictionListener<K, VersionedItem<V>> {

		/** The eviction listener. */
		EvictionListener<K, V> evictionListener;

		/**
		 * Instantiates a new cache item eviction listener.
		 * 
		 * @param evictionListener
		 *            the eviction listener
		 */
		public CacheItemEvictionListener(EvictionListener<K, V> evictionListener) {
			this.evictionListener = evictionListener;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cetsoft.imcache.cache.EvictionListener#onEviction(java.lang.Object
		 * , java.lang.Object)
		 */
		public void onEviction(K key, VersionedItem<V> value) {
			evictionListener.onEviction(key, value.getValue());
		}
	}

	/**
	 * The Class CacheItemCacheLoader.
	 * 
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	protected static class CacheItemCacheLoader<K, V> implements CacheLoader<K, VersionedItem<V>> {

		/** The cache loader. */
		CacheLoader<K, V> cacheLoader;

		/**
		 * Instantiates a new cache item cache loader.
		 * 
		 * @param cacheLoader the cache loader
		 */
		public CacheItemCacheLoader(CacheLoader<K, V> cacheLoader) {
			this.cacheLoader = cacheLoader;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.CacheLoader#load(java.lang.Object)
		 */
		public VersionedItem<V> load(K key) {
			V value = cacheLoader.load(key);
			if (value == null) {
				return null;
			}
			return new SimpleItem<V>(value);
		}

	}

	/**
	 * The Class CacheItemSerializer.
	 * 
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	protected static class CacheItemSerializer<V> implements Serializer<VersionedItem<V>> {

		/** The serializer. */
		Serializer<V> serializer;

		/**
		 * Instantiates a new cache item serializer.
		 * 
		 * @param serializer the serializer
		 */
		public CacheItemSerializer(Serializer<V> serializer) {
			this.serializer = serializer;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cetsoft.imcache.serialization.Serializer#serialize(java.lang.
		 * Object)
		 */
		public byte[] serialize(VersionedItem<V> value) {
			byte[] payload = serializer.serialize(value.getValue());
			byte[] newPayload = new byte[payload.length + 4];
			System.arraycopy(payload, 0, newPayload, 0, payload.length);
			System.arraycopy(SerializationUtils.serializeInt(value.getVersion()), 0, newPayload, payload.length, 4);
			return newPayload;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.serialization.Serializer#deserialize(byte[])
		 */
		public VersionedItem<V> deserialize(byte[] payload) {
			byte[] newPayload = new byte[payload.length - 4];
			byte[] version = new byte[4];
			System.arraycopy(payload, 0, newPayload, 0, payload.length - 4);
			System.arraycopy(payload, payload.length - 4, version, 0, 4);
			SimpleItem<V> cacheItem = new SimpleItem<V>(serializer.deserialize(newPayload));
			cacheItem.setVersion(SerializationUtils.deserializeInt(version));
			return cacheItem;
		}

	}

	/**
	 * The Class CacheItemIndexHandler.
	 * 
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	protected static class CacheItemIndexHandler<K, V> implements IndexHandler<K, VersionedItem<V>> {

		/** The query executer. */
		IndexHandler<K, V> indexHandler;

		/**
		 * Instantiates a new cache item query executer.
		 * 
		 * @param indexHandler the query executer
		 */
		public CacheItemIndexHandler(IndexHandler<K, V> indexHandler) {
			this.indexHandler = indexHandler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cetsoft.imcache.cache.search.Indexable#addIndex(com.cetsoft.imcache
		 * .cache.search.CacheIndex)
		 */
		public void addIndex(String attributeName, IndexType type) {
			indexHandler.addIndex(attributeName, type);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cetsoft.imcache.cache.search.IndexHandler#add(java.lang.Object,
		 * java.lang.Object)
		 */
		public void add(K key, VersionedItem<V> value) {
			this.indexHandler.add(key, value.getValue());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cetsoft.imcache.cache.search.IndexHandler#remove(java.lang.Object
		 * , java.lang.Object)
		 */
		public void remove(K key, VersionedItem<V> value) {
			this.indexHandler.remove(key, value.getValue());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.cetsoft.imcache.cache.search.IndexHandler#clear()
		 */
		public void clear() {
			this.indexHandler.clear();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cetsoft.imcache.cache.search.IndexHandler#execute(com.cetsoft
		 * .imcache.cache.search.Query)
		 */
		public List<K> execute(Query query) {
			return indexHandler.execute(query);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#getName()
	 */
	public String getName() {
		if (this.name != null) {
			return name;
		}
		return this.getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

}
