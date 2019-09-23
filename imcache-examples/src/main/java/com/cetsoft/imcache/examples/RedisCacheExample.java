/**
 * Copyright © 2013 Cetsoft. All rights reserved.
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
 */
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import java.util.concurrent.TimeUnit;

public class RedisCacheExample {

  static final String HOSTNAME = "localhost";
  static final int PORT = 6379;

  public static void example() throws InterruptedException {
    //If hostname and port aren't given, default port and
    //hostname will be used.
    Cache<Integer, String> cache = CacheBuilder.redisCache().hostName(HOSTNAME).port(PORT).build();
    cache.put(1, "apple", TimeUnit.MILLISECONDS, 10000);
    Thread.sleep(100);
    System.out.println(cache.get(1));
  }

  public static void main(String[] args) throws InterruptedException {
    example();
  }

}
