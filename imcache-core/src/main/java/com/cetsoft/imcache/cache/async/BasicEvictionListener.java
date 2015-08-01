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
* Date   : Jun 5, 2014
*/
package com.cetsoft.imcache.cache.async;

import java.util.concurrent.atomic.AtomicInteger;

import com.cetsoft.imcache.cache.util.ThreadUtils;

/**
 * The basic eviction listener interface for receiving eviction events. 
 * When eviction occurs, this class creates a thread to save the data.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public abstract class BasicEvictionListener<K, V> implements AsyncEvictionListener<K, V> {

	/** The Constant NO_OF_EVICTION_LISTENERS. */
	private static final AtomicInteger NO_OF_EVICTION_LISTENERS = new AtomicInteger();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.cache.EvictionListener#onEviction(java.lang.Object,
	 * java.lang.Object)
	 */
	public void onEviction(final K key, final V value) {
		ThreadUtils.createDaemonThread(new Runnable() {
			public void run() {
				save(key, value);
			}
		}, "imcache:basicAsyncEvictionListener(thread=" + NO_OF_EVICTION_LISTENERS.incrementAndGet() + ")")
		.start();
	}

	/**
	 * Saves the key value pair.
	 *
	 * @param key the key
	 * @param value the value
	 */
	abstract void save(K key, V value);

}
