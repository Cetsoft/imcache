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
 * The Class Pointer is a pointer to the stored elements, which keeps
 * position of the payload and related OffHeapByteBuffer. Additionally,
 * it keeps track of access time of the payload.
 */
public class Pointer {

	/** The position. */
	protected int position;

	/** The access time. */
	protected long accessTime;

	/** The off heap byte buffer. */
	protected OffHeapByteBuffer offHeapByteBuffer;

	/**
	 * Instantiates a new pointer.
	 *
	 * @param position the position
	 * @param offHeapByteBuffer the off heap byte buffer
	 */
	public Pointer(int position, OffHeapByteBuffer offHeapByteBuffer) {
		this(position, System.currentTimeMillis(), offHeapByteBuffer);
	}

	/**
	 * Instantiates a new pointer.
	 *
	 * @param position the position
	 * @param accessTime the access time
	 * @param offHeapByteBuffer the off heap byte buffer
	 */
	public Pointer(int position, long accessTime, OffHeapByteBuffer offHeapByteBuffer) {
		this.position = position;
		this.accessTime = accessTime;
		this.offHeapByteBuffer = offHeapByteBuffer;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Gets the access time.
	 *
	 * @return the access time
	 */
	public long getAccessTime() {
		return accessTime;
	}

	/**
	 * Sets the access time.
	 *
	 * @param accessTime the new access time
	 */
	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}

	/**
	 * Gets the off heap byte buffer.
	 *
	 * @return the off heap byte buffer
	 */
	public OffHeapByteBuffer getOffHeapByteBuffer() {
		return offHeapByteBuffer;
	}

	/**
	 * Sets the off heap byte buffer.
	 *
	 * @param offHeapByteBuffer the new off heap byte buffer
	 */
	public void setOffHeapByteBuffer(OffHeapByteBuffer offHeapByteBuffer) {
		this.offHeapByteBuffer = offHeapByteBuffer;
	}

	/**
	 * Copies given pointer.
	 *
	 * @param pointer the pointer
	 * @return the pointer
	 */
	public Pointer copy(Pointer pointer) {
		this.accessTime = pointer.accessTime;
		this.offHeapByteBuffer = pointer.offHeapByteBuffer;
		this.position = pointer.position;
		return this;
	}
}
