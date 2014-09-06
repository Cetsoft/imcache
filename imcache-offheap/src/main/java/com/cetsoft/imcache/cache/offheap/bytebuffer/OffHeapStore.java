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
 * The Interface OffHeapStore.
 */
public interface OffHeapStore {

	/**
	 * Retrieves the payload associated with the pointer.
	 *
	 * @param pointer the pointer
	 * @return the byte[]
	 */
	byte[] retrieve(Pointer pointer);

	/**
	 * Removes the payload and marks it as dirty.
	 *
	 * @param pointer the pointer
	 * @return the byte[]
	 */
	byte[] remove(Pointer pointer);

	/**
	 * Stores the payload.
	 *
	 * @param payload the payload
	 * @return the pointer
	 */
	Pointer store(byte[] payload);

	/**
	 * Updates the payload by marking exPayload as dirty.
	 *
	 * @param pointer the pointer
	 * @param payload the payload
	 * @return the pointer
	 */
	Pointer update(Pointer pointer, byte[] payload);

	/**
	 * Calculates and returns Dirty memory.
	 *
	 * @return the long
	 */
	long dirtyMemory();

	/**
	 *  Calculates and returns Used memory.
	 *
	 * @return the long
	 */
	long usedMemory();

	/**
	 *  Calculates and returns Free memory.
	 *
	 * @return the long
	 */
	long freeMemory();

	/**
	 * Frees the memory.
	 */
	void free();
}
