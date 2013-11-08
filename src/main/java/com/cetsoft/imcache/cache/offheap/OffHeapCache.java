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
 * Date   : Sep 20, 2013
 */
package com.cetsoft.imcache.cache.offheap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.bytebuffer.Pointer;
import com.cetsoft.imcache.cache.AbstractCache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.concurrent.lock.StripedReadWriteLock;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class OffHeapCache is a cache that uses offheap byte buffers
 * to store or retrieve data by serializing items into bytes. To do so,
 * OffHeapCache uses pointers to point array location of an item.
 * OffHeapCache clears the buffers periodically to gain free space if
 * buffers are dirty(unused memory). It also does eviction depending on
 * access time to the objects.
 * 
 * @param <K> the key type
 * @param <V> the value type
 */
public class OffHeapCache<K, V> extends AbstractCache<K, V> {

	/** The Constant DELTA. */
	private static final float DELTA = 0.00001f;
	
	/** The default buffer cleaner period which is 10 minutes. */
	public static final long DEFAULT_BUFFER_CLEANER_PERIOD = 10 * 60 * 1000;
	
	/** The default eviction period which is 10 minutes. */
	public static final long DEFAULT_EVICTION_PERIOD = 10 * 60 * 1000;

	/** The default buffer cleaner threshold. */
	public static final float DEFAULT_BUFFER_CLEANER_THRESHOLD = 0.5f;
	
	/** The Constant DEFAULT_CONCURRENCY_LEVEL. */
	public static final int DEFAULT_CONCURRENCY_LEVEL = 4;

	/** The hit. */
	protected AtomicLong hit = new AtomicLong();

	/** The miss. */
	protected AtomicLong miss = new AtomicLong();

	/** The pointer map. */
	private ConcurrentMap<K, Pointer> pointerMap = new ConcurrentHashMap<K, Pointer>();

	/** The serializer. */
	private Serializer<V> serializer;

	/** The buffer store. */
	private OffHeapByteBufferStore bufferStore;

	/** The read write lock. */
	private StripedReadWriteLock readWriteLock;

	/** The Constant NO_OF_CLEANERS. */
	private static final AtomicInteger NO_OF_CLEANERS = new AtomicInteger();
	
	/** The Constant NO_OF_EVICTORS. */
	private static final AtomicInteger NO_OF_EVICTORS = new AtomicInteger();
	
	/**
	 * Instantiates a new offheap cache.
	 *
	 * @param cacheLoader the cache loader
	 * @param evictionListener the eviction listener
	 * @param indexHandler the query executer
	 * @param byteBufferStore the byte buffer store
	 * @param serializer the serializer
	 * @param bufferCleanerPeriod the buffer cleaner period
	 * @param bufferCleanerThreshold the buffer cleaner threshold
	 * @param concurrencyLevel the concurrency level
	 * @param evictionPeriod the eviction period
	 */
	public OffHeapCache(CacheLoader<K, V> cacheLoader, EvictionListener<K, V> evictionListener,IndexHandler<K, V> indexHandler,
			OffHeapByteBufferStore byteBufferStore, Serializer<V> serializer,long bufferCleanerPeriod, 
			final float bufferCleanerThreshold, int concurrencyLevel, final long evictionPeriod) {
		super(cacheLoader,evictionListener,indexHandler);
		initCache(byteBufferStore, serializer, bufferCleanerPeriod, bufferCleanerThreshold, concurrencyLevel, evictionPeriod);
	}
	
	/**
	 * Inits the cache.
	 *
	 * @param byteBufferStore the byte buffer store
	 * @param serializer the serializer
	 * @param bufferCleanerPeriod the buffer cleaner period
	 * @param bufferCleanerThreshold the buffer cleaner threshol
	 * @param concurrencyLevel the concurrency level
	 * @param evictionPeriod the eviction period
	 */
	protected void initCache(OffHeapByteBufferStore byteBufferStore, Serializer<V> serializer,
			long bufferCleanerPeriod, final float bufferCleanerThreshold, int concurrencyLevel, final long evictionPeriod) {
		if(concurrencyLevel>11||concurrencyLevel<0){
			throw new IllegalArgumentException("ConcurrencyLevel must be between 0 and 11 inclusive!");
		}
		this.serializer = serializer;
		this.bufferStore = byteBufferStore;
		this.readWriteLock = new StripedReadWriteLock(concurrencyLevel);
		ScheduledExecutorService cleanerService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			public Thread newThread(Runnable runnable) {
				return new Thread(runnable, "imcache:bufferCleaner("+ NO_OF_CLEANERS.incrementAndGet() + ")");
			}
		});
		cleanerService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				Collection<Pointer> pointers = pointerMap.values();
				List<Pointer> pointersToBeRedistributed = new ArrayList<Pointer>();
				Map<OffHeapByteBuffer, Float> buffers = new HashMap<OffHeapByteBuffer, Float>();
				Set<Integer> buffersToBeCleaned = new HashSet<Integer>();
				for (Pointer pointer : pointers) {
					Float ratio = buffers.get(pointer.getOffHeapByteBuffer());
					if (ratio == null) {
						ratio = (float) (pointer.getOffHeapByteBuffer().dirtyMemory() / (pointer.getOffHeapByteBuffer().freeMemory()
								+ pointer.getOffHeapByteBuffer().usedMemory() + pointer.getOffHeapByteBuffer().dirtyMemory()));
						buffers.put(pointer.getOffHeapByteBuffer(), ratio);
					}
					if (ratio - bufferCleanerThreshold > DELTA) {
						pointersToBeRedistributed.add(pointer);
						buffersToBeCleaned.add(pointer.getOffHeapByteBuffer().getIndex());
					}
				}
				bufferStore.redistribute(pointersToBeRedistributed);
				for (Integer bufferIndex : buffersToBeCleaned) {
					bufferStore.free(bufferIndex);
				}
			}

		}, 0, bufferCleanerPeriod, TimeUnit.MILLISECONDS);
		ScheduledExecutorService evictionService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			public Thread newThread(Runnable runnable) {
				return new Thread(runnable, "imcache:evictor("+ NO_OF_EVICTORS.incrementAndGet() + ")");
			}
		});
		evictionService.scheduleAtFixedRate(new Runnable() {
			public void run() {
				Set<Entry<K,Pointer>> entries = pointerMap.entrySet();
				for (Entry<K,Pointer> entry : entries) {
					synchronized (entry.getValue()) {
						if(entry.getValue().getAccessTime()+evictionPeriod<System.currentTimeMillis()){
							invalidate(entry.getKey());
						}
					}
				}
			}
		}, 0, evictionPeriod, TimeUnit.MILLISECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#put(java.lang.Object, java.lang.Object)
	 */
	public void put(K key, V value) {
		writeLock(key);
		Pointer pointer = pointerMap.get(key);
		try {
			if (pointer == null) {
				pointer = bufferStore.store(serializer.serialize(value));
			} else {
				synchronized (pointer) {
					pointer = bufferStore.update(pointer, serializer.serialize(value));
				}
			}
			pointerMap.put(key, pointer);
		} finally {
			writeUnlock(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#get(java.lang.Object)
	 */
	public V get(K key) {
		readLock(key);
		try {
			Pointer pointer = pointerMap.get(key);
			if (pointer != null) {
				hit.incrementAndGet();
				synchronized (pointer) {
					byte[] payload = bufferStore.retrieve(pointer);
					return serializer.deserialize(payload);
				}

			} else {
				miss.incrementAndGet();
				V value = cacheLoader.load(key);
				if (value != null) {
					put(key, value);
				}
				return value;
			}
		} finally {
			readUnlock(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#invalidate(java.lang.Object)
	 */
	public V invalidate(K key) {
		writeLock(key);
		try{
			Pointer pointer = pointerMap.get(key);
			if (pointer != null) {
				synchronized (pointer) {
					byte[] payload = bufferStore.remove(pointer);
					return serializer.deserialize(payload);
				}
			}
		}finally{
			writeUnlock(key);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#contains(java.lang.Object)
	 */
	public boolean contains(K key) {
		return pointerMap.containsKey(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#clear()
	 */
	public void clear() {
		pointerMap.clear();
		bufferStore.free();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.cache.Cache#hitRatio()
	 */
	public double hitRatio() {
		return hit.get() / (hit.get() + miss.get());
	}

	/**
	 * Read Lock for key is locked.
	 * 
	 * @param key the key
	 */
	protected void readLock(K key) {
		readWriteLock.readLock(Math.abs(key.hashCode()));
	}

	/**
	 * Read Lock for key is unlocked.
	 * 
	 * @param key the key
	 */
	protected void readUnlock(K key) {
		readWriteLock.readUnlock(Math.abs(key.hashCode()));
	}

	/**
	 * Write Lock for key is locked..
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
	
}
