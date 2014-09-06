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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.SimpleItem;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.offheap.StaleItemException;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferStore;

/**
 * The Class VersionedOffHeapCacheExample.
 */
public class VersionedOffHeapCacheExample {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String [] args){
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(10000,10); 
		final Cache<String, VersionedItem<String>> cache = CacheBuilder.versionedOffHeapCache().
		storage(bufferStore).build();
		ExecutorService service = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 100000; i++) {
			service.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					while(true){
						try{
							VersionedItem<String> value = cache.get("apple");
							String newValue = getRandomString();
							if(value==null){
								cache.put("apple", new SimpleItem<String>(newValue));
							}
							else{
								value.update(newValue);
								cache.put("apple", value);
							}
							System.out.println(value.getValue());
							break;
						}catch(StaleItemException ex){
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
	private static String getRandomString(){
		char[] chars = new char[5];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (Math.random()*25+65);
		}
		return new String(chars);
	}
}
