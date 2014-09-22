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
* Date   : Sep 4, 2014
*/
package com.cetsoft.imcache.cache.redis.client;

import com.cetsoft.imcache.cache.redis.client.util.SafeEncoder;

/**
 * A light weight implementation of Redis Client.
 */
public class RedisClient extends BinaryClient {

    public RedisClient(String host) {
        super(host);
    }

    public RedisClient(String host, int port) {
        super(host, port);
    }

    public String set(final String key, final String value) {
        set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        return getStatusCodeReply();
    }
}
