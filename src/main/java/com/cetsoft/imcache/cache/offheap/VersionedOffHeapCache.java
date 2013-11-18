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
* Date   : Sep 26, 2013
*/
package com.cetsoft.imcache.cache.offheap;

import java.util.List;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.SimpleItem;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class VersionedOffHeapCache is a type of offheap cache where cache items
 * have versions that are incremented for each update.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class VersionedOffHeapCache<K, V> implements SearchableCache<K, VersionedItem<V>>{

	/** The off heap cache. */
	protected OffHeapCache<K, VersionedItem<V>> offHeapCache;
	
	/**
	 * Instantiates a new versioned off heap cache.
	 *
	 * @param serializer the serializer
	 * @param byteBufferStore the byte buffer store
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the query executer
	 * @param bufferCleanerPeriod the buffer cleaner period
	 * @param bufferCleanerThreshold the buffer cleaner threshold
	 * @param concurrencyLevel the concurrency level
	 * @param evictionPeriod the eviction period
	 */
	public VersionedOffHeapCache(Serializer<VersionedItem<V>> serializer, OffHeapByteBufferStore byteBufferStore, 
			CacheLoader<K, VersionedItem<V>> cacheLoader, EvictionListener<K, VersionedItem<V>> evictionListener,
			IndexHandler<K, VersionedItem<V>> indexHandler,long bufferCleanerPeriod, float bufferCleanerThreshold, int concurrencyLevel, 
			long evictionPeriod) {
		offHeapCache = new OffHeapCache<K, VersionedItem<V>>(cacheLoader, evictionListener, indexHandler,byteBufferStore, serializer, 
				bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
	}
//	
	/**
 * Instantiates a new versioned off heap cache.
 *
 * @param byteBufferStore the byte buffer store
 * @param serializer the serializer
 * @param cacheLoader the cache loader
 * @param evictionListener the eviction listener
 * @param indexHandler the query executer
 * @param bufferCleanerPeriod the buffer cleaner period
 * @param bufferCleanerThreshold the buffer cleaner threshold
 * @param concurrencyLevel the concurrency level
 * @param evictionPeriod the eviction period
 */
	public VersionedOffHeapCache(OffHeapByteBufferStore byteBufferStore, Serializer<V> serializer,CacheLoader<K,V> cacheLoader, 
			EvictionListener<K, V> evictionListener, IndexHandler<K, V> indexHandler,long bufferCleanerPeriod,float bufferCleanerThreshold, int concurrencyLevel, long evictionPeriod) {
		this(new CacheItemSerializer<K, V>(serializer),byteBufferStore,new CacheItemCacheLoader<K, V>(cacheLoader),
				new CacheItemEvictionListener<K, V>(evictionListener),new CacheItemIndexHandler<K,V>(indexHandler),bufferCleanerPeriod,
				bufferCleanerThreshold,concurrencyLevel, evictionPeriod);
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	public void put(K key, VersionedItem<V> value) {
		int version = value.getVersion();
		VersionedItem<V> exValue =  get(key);
		if(exValue!=null&&version!=exValue.getVersion()){
			throw new StaleItemException(version,exValue.getVersion());
		}
		version++;
		synchronized (value) {
			if(value.getVersion()==version){
				throw new StaleItemException(version,exValue.getVersion());
			}
			value.setVersion(version);
		}
		offHeapCache.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#get(java.lang.Object)
	 */
	public VersionedItem<V> get(K key) {
		return offHeapCache.get(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#invalidate(java.lang.Object)
	 */
	public VersionedItem<V> invalidate(K key) {
		return offHeapCache.invalidate(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(K key) {
		return offHeapCache.contains(key);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#clear()
	 */
	public void clear() {
		offHeapCache.clear();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.Cache#hitRatio()
	 */
	public double hitRatio() {
		return offHeapCache.hitRatio();
	}
	
	public List<VersionedItem<V>> execute(Query query) {
		return offHeapCache.execute(query);
	}

	/**
	 * The listener interface for receiving cacheItemEviction events.
	 * The class that is interested in processing a cacheItemEviction
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the constructors.
	 * When the cacheItemEviction event occurs, that object's appropriate
	 * method is invoked.
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
		 * @param evictionListener the eviction listener
		 */
		public CacheItemEvictionListener(EvictionListener<K, V> evictionListener) {
			this.evictionListener = evictionListener;
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.EvictionListener#onEviction(java.lang.Object, java.lang.Object)
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
	private static class CacheItemCacheLoader<K,V> implements CacheLoader<K, VersionedItem<V>>{

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

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.CacheLoader#load(java.lang.Object)
		 */
		public VersionedItem<V> load(K key) {
			V value = cacheLoader.load(key);
			if(value==null){
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
	private static class CacheItemSerializer<K,V> implements Serializer<VersionedItem<V>>{
		
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

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.serialization.Serializer#serialize(java.lang.Object)
		 */
		public byte[] serialize(VersionedItem<V> value) {
			byte[] payload = serializer.serialize(value.getValue());
			byte[] newPayload = new byte[payload.length+4];
			System.arraycopy(payload, 0, newPayload, 0, payload.length);
			System.arraycopy(serializeVersion(value.getVersion()), 0, newPayload, payload.length, 4);
			return newPayload;
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.serialization.Serializer#deserialize(byte[])
		 */
		public VersionedItem<V> deserialize(byte[] payload) {
			byte[] newPayload = new byte[payload.length-4];
			byte[] version = new byte[4];
			System.arraycopy(payload, 0, newPayload, 0, payload.length-4);
			System.arraycopy(payload, payload.length-4, version, 0,4);
			SimpleItem<V> cacheItem = new SimpleItem<V>(serializer.deserialize(newPayload));
			cacheItem.setVersion(deserializeVersion(version));
			return cacheItem;
		}
		
		/**
		 * Serialize version.
		 *
		 * @param version the version
		 * @return the byte[]
		 */
		private byte[] serializeVersion(int version){
			byte[] ret = new byte[4];
		    ret[3] = (byte) (version & 0xFF);   
		    ret[2] = (byte) ((version >> 8) & 0xFF);   
		    ret[1] = (byte) ((version >> 16) & 0xFF);   
		    ret[0] = (byte) ((version >> 24) & 0xFF);
		    return ret;
		}
		
		/**
		 * Deserialize version.
		 *
		 * @param version the version
		 * @return the int
		 */
		private int deserializeVersion(byte[] version){
		    int value = 0;
		    for (int i = 0; i < 4; i++) {
		        int shift = (4 - 1 - i) * 8;
		        value += (version[i] & 0x000000FF) << shift;
		    }
		    return value;
		}
		
	}
	
	/**
	 * The Class CacheItemIndexHandler.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	private static class CacheItemIndexHandler<K,V> implements IndexHandler<K,VersionedItem<V>>{
		
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

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.search.Indexable#addIndex(com.cetsoft.imcache.cache.search.CacheIndex)
		 */
		public void addIndex(String attributeName, IndexType type) {
			indexHandler.addIndex(attributeName,type);
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.search.IndexHandler#add(java.lang.Object, java.lang.Object)
		 */
		public void add(K key, VersionedItem<V> value) {
			this.indexHandler.add(key, value.getValue());
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.search.IndexHandler#remove(java.lang.Object, java.lang.Object)
		 */
		public void remove(K key, VersionedItem<V> value) {
			this.indexHandler.remove(key, value.getValue());
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.search.IndexHandler#clear()
		 */
		public void clear() {
			this.indexHandler.clear();
		}

		/* (non-Javadoc)
		 * @see com.cetsoft.imcache.cache.search.IndexHandler#execute(com.cetsoft.imcache.cache.search.Query)
		 */
		public List<K> execute(Query query) {
			return indexHandler.execute(query);
		}
		
	}

}
