/*
* Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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
