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
 * Date   : May 20, 2014
 */
package com.cetsoft.imcache.examples;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.pupulator.SimpleCachePupulator;
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
		new SimpleCachePupulator<String, String>(cache) {
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
