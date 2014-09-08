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