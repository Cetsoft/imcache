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
