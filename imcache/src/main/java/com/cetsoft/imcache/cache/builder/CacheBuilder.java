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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.cache.builder;

/**
 * The Class CacheBuilder.
 */
public abstract class CacheBuilder {

	/**
	 * Heap cache.
	 *
	 * @return the heap cache builder
	 */
	public static HeapCacheBuilder heapCache() {
		return new HeapCacheBuilder();
	}

	/**
	 * Transactional Heap cache.
	 *
	 * @return the transactional heap cache builder
	 */
	public static TransactionalHeapCacheBuilder transactionalHeapCache() {
		return new TransactionalHeapCacheBuilder();
	}

	/**
	 * Concurrent heap cache.
	 *
	 * @return the concurrent heap cache builder
	 */
	public static ConcurrentHeapCacheBuilder concurrentHeapCache() {
		return new ConcurrentHeapCacheBuilder();
	}

	/**
	 * Off heap cache.
	 *
	 * @return the off heap cache builder
	 */
	public static OffHeapCacheBuilder offHeapCache() {
		return new OffHeapCacheBuilder();
	}

	/**
	 * Versioned Off heap cache.
	 *
	 * @return the off heap cache builder
	 */
	public static VersionedOffHeapCacheBuilder versionedOffHeapCache() {
		return new VersionedOffHeapCacheBuilder();
	}
	
	/**
	 * Redis cache.
	 *
	 * @return the redis cache builder
	 */
	public static RedisCacheBuilder redisCache(){
		return new RedisCacheBuilder();
	}

}
