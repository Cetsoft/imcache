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
* Date   : Jun 5, 2014
*/
package com.cetsoft.imcache.cache.async;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The basic eviction listener interface for receiving eviction events. 
 * When eviction occurs, this class creates a thread to save the data.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class BasicEvictionListener<K,V> implements AsyncEvictionListener<K, V>{

	/** The Constant NO_OF_EVICTION_LISTENERS. */
	private static final AtomicInteger NO_OF_EVICTION_LISTENERS = new AtomicInteger();
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.EvictionListener#onEviction(java.lang.Object, java.lang.Object)
	 */
	public void onEviction(final K key, final V value) {
		new Thread(new Runnable() {
			public void run() {
				save(key, value);
			}
		},"imcache:basicAsyncEvictionListener(thread="+ NO_OF_EVICTION_LISTENERS.incrementAndGet() + ")").start();
	}
	
	/**
	 * Saves the key value pair.
	 *
	 * @param key the key
	 * @param value the value
	 */
	abstract void save(K key, V value);
	
}
