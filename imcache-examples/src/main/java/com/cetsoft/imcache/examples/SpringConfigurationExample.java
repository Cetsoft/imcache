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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.cetsoft.imcache.cache.populator.SimpleCachePopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.util.CacheUtils;

/**
 * The Class SpringConfigurationExample.
 */
@Component
public class SpringConfigurationExample {

	/** The cache. */
	@Autowired
	Cache<String, String> cache;

	/** The cache dao. */
	final CacheDao cacheDao = new CacheDaoImpl();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		@SuppressWarnings({ "resource", "unused" })
		ApplicationContext context = new ClassPathXmlApplicationContext("exampleContext.xml");
	}

	/**
	 * Inits the cache.
	 */
	@PostConstruct
	public void initCache() {
		new SimpleCachePopulator<String, String>(cache) {
			public List<CacheEntry<String, String>> loadEntries() {
				List<CacheEntry<String, String>> cacheEntries = new ArrayList<CacheEntry<String, String>>();
				for (String cacheEntry : cacheDao.getAll()) {
					cacheEntries.add(CacheUtils.createEntry(cacheEntry, cacheEntry));
				}
				return cacheEntries;
			}
		}.pupulate();
		System.out.println(cache.get("orange"));
	}

	/**
	 * The Interface CacheDao.
	 */
	protected static interface CacheDao {

		/**
		 * Gets the all.
		 *
		 * @return the all
		 */
		List<String> getAll();
	}

	/**
	 * The Class CacheDaoImpl.
	 */
	protected static class CacheDaoImpl implements CacheDao {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.cetsoft.imcache.examples.SpringConfigurationExample.CacheDao#
		 * getAll()
		 */
		public List<String> getAll() {
			List<String> fruits = new ArrayList<String>();
			fruits.add("orange");
			fruits.add("apple");
			fruits.add("kiwi");
			return fruits;
		}
	}
}
