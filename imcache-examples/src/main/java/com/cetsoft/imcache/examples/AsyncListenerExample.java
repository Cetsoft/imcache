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
* Date   : Jun 7, 2014
*/
package com.cetsoft.imcache.examples;

import java.util.List;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.async.CacheTask;
import com.cetsoft.imcache.cache.async.ScheduledEvictionListener;
import com.cetsoft.imcache.cache.builder.CacheBuilder;

/**
 * The Class AsyncListenerExample.
 */
public class AsyncListenerExample {

	public static void example() {
		final StringDAO stringDAO = new StringDAO();
		Cache<String, String> cache = CacheBuilder.concurrentHeapCache()
				.evictionListener(new ScheduledEvictionListener<String, String>() {
					@Override
					public void save(List<CacheTask<String, String>> cacheTasks) {
						for (CacheTask<String, String> task : cacheTasks) {
							stringDAO.update(task.getKey(), task.getValue());
						}
					}
				}).build();
		cache.put("key", "value");
	}
	
	/**
	 * The Class StringDAO.
	 */
	private static class StringDAO {

		public void update(String key, String value) {}
	}
	
	public static void main(String[] args) {
		example();
	}

}
