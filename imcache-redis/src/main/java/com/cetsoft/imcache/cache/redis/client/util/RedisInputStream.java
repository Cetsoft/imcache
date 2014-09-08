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