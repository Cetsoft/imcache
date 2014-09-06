/*
 * Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
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

		/**
		 * Update.
		 *
		 * @param key the key
		 * @param value the value
		 */
		public void update(String key, String value) {
		}
	}

}
