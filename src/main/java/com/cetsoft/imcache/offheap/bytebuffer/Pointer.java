/*
* Copyright (C) 2013 Cetsoft, http://www.cetsoft.com
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
}
