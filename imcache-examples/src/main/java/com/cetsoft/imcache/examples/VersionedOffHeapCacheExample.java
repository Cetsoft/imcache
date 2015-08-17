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
* Date   : May 20, 2014
*/
package com.cetsoft.imcache.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.SimpleItem;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.offheap.StaleItemException;
import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBufferStore;

/**
 * The Class VersionedOffHeapCacheExample.
 */
public class VersionedOffHeapCacheExample {

	public static void example() {
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(10000, 10);
		final Cache<String, VersionedItem<String>> cache = CacheBuilder.versionedOffHeapCache().storage(bufferStore)
				.build();
		ExecutorService service = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 100000; i++) {
			service.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					while (true) {
						try {
							VersionedItem<String> value = cache.get("apple");
							String newValue = getRandomString();
							if (value == null) {
								cache.put("apple", new SimpleItem<String>(newValue));
							} else {
								value.update(newValue);
								cache.put("apple", value);
							}
							System.out.println(cache.get("apple").getValue());
							break;
						} catch (StaleItemException ex) {
							ex.printStackTrace();
						}
					}
					VersionedItem<String> item = cache.get("apple");
					System.out.println(item.getVersion());
				}
			});
		}
	}

	/**
	 * Gets the random string.
	 *
	 * @return the random string
	 */
	private static String getRandomString() {
		char[] chars = new char[5];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (Math.random() * 25 + 65);
		}
		return new String(chars);
	}
	
	public static void main(String[] args) {
		example();
	}
}
