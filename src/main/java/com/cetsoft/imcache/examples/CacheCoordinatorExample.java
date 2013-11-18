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
* Date   : Nov 15, 2013
*/
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheType;
import com.cetsoft.imcache.cache.ImcacheType;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.coordinator.CacheCoordinator;
import com.cetsoft.imcache.cache.coordinator.CacheFactory;
import com.cetsoft.imcache.cache.coordinator.LocalCacheCoordinator;

public class CacheCoordinatorExample {
	
	private static CacheType<String, String> strCacheType = new ImcacheType<String, String>();
	private static CacheFactory factory = new CacheFactory() {
		public <K, V> Cache<K, V> create() {
			return CacheBuilder.heapCache().build();
		}
	};
	
	public static void main(String[] args) {
		final CacheCoordinator cacheCoordinator = new LocalCacheCoordinator(factory);
		final CacheTypeTest test = new CacheTypeTest(cacheCoordinator);
		for(int i=0;i<3;i++){
			new Thread(new Runnable() {
				public void run() {
					test.setValue(strCacheType,Thread.currentThread().getName(),Thread.currentThread().getName());
					System.out.println(test.getValue(strCacheType, Thread.currentThread().getName()));
				}
			},"Thread #"+i).start();
		}
	}
	
	private static class CacheTypeTest{
		
		final CacheCoordinator cacheCoordinator;
		
		public CacheTypeTest(CacheCoordinator cacheCoordinator) {
			this.cacheCoordinator = cacheCoordinator;
		}

		public void setValue(CacheType<String,String> type, String key, String value){
			cacheCoordinator.getCache(type).put(key, value);
		}
		
		public String getValue(CacheType<String,String> type, String key){
			return cacheCoordinator.getCache(type).get(key);
		}
	}
	
}
