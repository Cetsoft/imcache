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
* Date   : Aug 18, 2015
*/
package com.cetsoft.imcache.cache.builder;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.redis.RedisCache;

/**
 * The Class RedisCacheBuilderTest.
 */
public class RedisCacheBuilderTest {
	
	/**
	 * Builds the.
	 */
	@Test
	public void build(){
		Cache<Object, Object> cache = CacheBuilder.redisCache()
		.cacheLoader(AbstractCacheBuilder.CACHE_LOADER)
		.evictionListener(AbstractCacheBuilder.EVICTION_LISTENER)
		.serializer(AbstractCacheBuilder.SERIALIZER)
		.hostName("localhost")
		.port(6379)
		.concurrencyLevel(2)
		.build();
		assertTrue(cache instanceof RedisCache);
	}
}
