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
* Date   : Sep 8, 2014
*/
package com.cetsoft.imcache.cache.redis.client.util;

import com.cetsoft.imcache.cache.redis.client.exception.RedisCacheException;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The Class RedisInputStream.
 */
public class RedisInputStream extends FilterInputStream {

	/** The buf. */
	protected final byte buf[];

	/** The limit. */
	protected int count, limit;

	/**
	 * Instantiates a new redis input stream.
	 *
	 * @param in the in
	 * @param size the size
	 */
	public RedisInputStream(InputStream in, int size) {
		super(in);
		if (size <= 0) {
			throw new IllegalArgumentException("Buffer size <= 0");
		}
		buf = new byte[size];
	}

	/**
	 * Instantiates a new redis input stream.
	 *
	 * @param in the in
	 */
	public RedisInputStream(InputStream in) {
		this(in, 8192);
	}

	/**
	 * Read byte.
	 *
	 * @return the byte
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte readByte() throws IOException {
		if (count == limit) {
			fill();
		}

		return buf[count++];
	}

	/**
	 * Read line.
	 *
	 * @return the string
	 */
	public String readLine() {
		int b;
		byte c;
		StringBuilder sb = new StringBuilder();

		try {
			while (true) {
				if (count == limit) {
					fill();
				}
				if (limit == -1)
					break;

				b = buf[count++];
				if (b == '\r') {
					if (count == limit) {
						fill();
					}

					if (limit == -1) {
						sb.append((char) b);
						break;
					}

					c = buf[count++];
					if (c == '\n') {
						break;
					}
					sb.append((char) b);
					sb.append((char) c);
				} else {
					sb.append((char) b);
				}
			}
		} catch (IOException e) {
			throw new RedisCacheException(e);
		}
		String reply = sb.toString();
		if (reply.length() == 0) {
			throw new RedisCacheException("It seems like server has closed the connection.");
		}
		return reply;
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		if (count == limit) {
			fill();
			if (limit == -1)
				return -1;
		}
		final int length = Math.min(limit - count, len);
		System.arraycopy(buf, count, b, off, length);
		count += length;
		return length;
	}

	/**
	 * Fill.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void fill() throws IOException {
		limit = in.read(buf);
		count = 0;
	}
}