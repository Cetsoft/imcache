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
 * Date   : Sep 22, 2013
 */
package com.cetsoft.imcache.cache.offheap.bytebuffer;

/**
 * The Interface ByteBuffer.
 */
public interface ByteBuffer {

	/**
	 * Gets the destination from the specified location.
	 *
	 * @param position the position
	 * @param destination the destination
	 * @param offset the offset
	 * @param length the length
	 */
	void get(int position, byte[] destination, int offset, int length);

	/**
	 * Puts source to the specified location of the ByteBuffer.
	 *
	 * @param position the position
	 * @param source the source
	 * @param offset the offset
	 * @param length the length
	 */
	void put(int position, byte[] source, int offset, int length);

	/**
	 * Frees the buffer.
	 */
	void free();
}
