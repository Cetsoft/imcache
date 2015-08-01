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
* Date   : Sep 8, 2014
*/
package com.cetsoft.imcache.cache.redis.client.exception;

/**
 * The Class RedisConnectionException is thrown when there is problem
 * with connection to redis.
 */
public class RedisConnectionException extends RedisCacheException {

	private static final long serialVersionUID = 6609362487613486027L;

	/**
     * Instantiates a new redis connection exception.
     *
     * @param message the message
     */
    public RedisConnectionException(String message) {
        super(message);
    }

    /**
     * Instantiates a new redis connection exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public RedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new redis connection exception.
     *
     * @param cause the cause
     */
    public RedisConnectionException(Throwable cause) {
        super(cause);
    }
}
