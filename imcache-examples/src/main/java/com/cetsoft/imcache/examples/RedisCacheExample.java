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
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;

public class RedisCacheExample {
	
	static final String HOSTNAME = "localhost";
	static final int PORT = 6379;

	public static void example() {
		//If hostname and port aren't given, default port and 
		//hostname will be used.
		Cache<Integer, String> cache = CacheBuilder.redisCache().
				hostName(HOSTNAME).port(PORT).build();
		cache.put(1, "apple");
		System.out.println(cache.get(1));
	}

	public static void main(String[] args) {
		example();
	}

}
