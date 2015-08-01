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
package com.cetsoft.imcache.cache.redis.client.util;

import com.cetsoft.imcache.cache.redis.client.Protocol;
import com.cetsoft.imcache.cache.redis.client.exception.RedisCacheException;

import java.io.UnsupportedEncodingException;

/**
 * The Class SafeEncoder encodes the given object to the bytes.
 */
public class SafeEncoder {
    
    /**
     * Encode many.
     *
     * @param strs the strs
     * @return the byte[][]
     */
    public static byte[][] encodeMany(final String... strs) {
        byte[][] many = new byte[strs.length][];
        for (int i = 0; i < strs.length; i++) {
            many[i] = encode(strs[i]);
        }
        return many;
    }

    /**
     * Encodes the given string.
     *
     * @param str the str
     * @return the byte[]
     */
    public static byte[] encode(final String str) {
        try {
            if (str == null) {
                throw new RedisCacheException("value sent to redis cannot be null");
            }
            return str.getBytes(Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RedisCacheException(e);
        }
    }

    /**
     * Encodes the given data.
     *
     * @param data the data
     * @return the string
     */
    public static String encode(final byte[] data) {
        try {
            return new String(data, Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RedisCacheException(e);
        }
    }
}