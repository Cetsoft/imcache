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
* Date   : Sep 22, 2013
*/
package com.cetsoft.imcache.offheap.bytebuffer;

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
