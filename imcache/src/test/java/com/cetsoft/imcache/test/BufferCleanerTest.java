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
* Date   : Jan 20, 2014
*/
package com.cetsoft.imcache.test;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferStore;

/**
 * The Class BufferCleanerTest.
 */
public class BufferCleanerTest {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws InterruptedException the interrupted exception
	 */
	public static void main(String[] args) throws InterruptedException {
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(500, 2);
		final Cache<Integer,Long> offHeapCache = CacheBuilder.offHeapCache().bufferCleanerPeriod(10).
		bufferCleanerThreshold(0.01f).storage(bufferStore).build();
		offHeapCache.put(0, 0L);
		offHeapCache.put(1, 1L);
		offHeapCache.put(2, 2L);
		offHeapCache.invalidate(0);
		offHeapCache.invalidate(1);
		Thread.sleep(10000);
	}
}
