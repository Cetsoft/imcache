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
package com.cetsoft.imcache.bytebuffer;

/**
 * The Class DirectByteBuffer.
 */
public class DirectByteBuffer implements ByteBuffer{
	
	/** The address. */
	private long address;
	
	/** The byte buffer. */
	private java.nio.ByteBuffer byteBuffer;
	
	/** The Constant UNSAFE. */
	private static final sun.misc.Unsafe UNSAFE = getUnsafe();
	
	/** The Constant UNSAFE_COPY_THRESHOLD. */
	private static final long UNSAFE_COPY_THRESHOLD = 1024L * 1024L;
	
	/** The Constant ARRAY_BASE_OFFSET. */
	private static final long ARRAY_BASE_OFFSET = (long) UNSAFE.arrayBaseOffset(byte[].class);
	
	/**
	 * Instantiates a new direct byte buffer.
	 *
	 * @param capacity the capacity
	 */
	public DirectByteBuffer(int capacity){
		byteBuffer = java.nio.ByteBuffer.allocateDirect(capacity);
		java.lang.reflect.Method method;
		try {
			// Get the actual address by calling address method
			method = byteBuffer.getClass().getDeclaredMethod("address");
			method.setAccessible(true);
			address = (Long) method.invoke(byteBuffer);
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.bytebuffer.ByteBuffer#get(int, byte[], int, int)
	 */
	public void get(int position, byte[] destination, int offset, int length) {
		copyToArray(getPosition(position), destination, ARRAY_BASE_OFFSET, offset, length);
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.bytebuffer.ByteBuffer#put(int, byte[], int, int)
	 */
	public void put(int position, byte[] source, int offset, int length) {
		copyFromArray(source, ARRAY_BASE_OFFSET, offset, getPosition(position), length);
	}

	/**
	 * Gets the position.
	 *
	 * @param position the position
	 * @return the position
	 */
	private long getPosition(int position) {
		return address + position;
	}

	/**
	 * Copy to array.
	 *
	 * @param sourceAddress the source address
	 * @param destination the destination
	 * @param destinationBaseOffset the destination base offset
	 * @param destinationPosition the destination position
	 * @param length the length
	 */
	private static void copyToArray(long sourceAddress, Object destination, long destinationBaseOffset, 
		long destinationPosition, long length) {
		// Calculate the distance from the beginning of the object up until a destinationPoint
		long offset = destinationBaseOffset + destinationPosition;
		while (length > 0) {
			// Copy as much as copy threshold
			long size = (length > UNSAFE_COPY_THRESHOLD) ? UNSAFE_COPY_THRESHOLD : length;
			// Copy from bytebuffer to destination
			UNSAFE.copyMemory(null, sourceAddress, destination, offset, size);
			length -= size;
			sourceAddress += size;
			offset += size;
		}
	}

	/**
	 * Copy from array.
	 *
	 * @param source the source
	 * @param sourceBaseOffset the source base offset
	 * @param sourcePosition the source position
	 * @param destinationAddress the destination address
	 * @param length the length
	 */
	private static void copyFromArray(Object source, long sourceBaseOffset, long sourcePosition, 
		long destinationAddress, long length) {
		// Calculate the distance from the beginning of the object up until a sourcePoint
		long offset = sourceBaseOffset + sourcePosition;
		while (length > 0) {
			// Copy as much as copy threshold
			long size = (length > UNSAFE_COPY_THRESHOLD) ? UNSAFE_COPY_THRESHOLD : length;
			// Copy from source to bytebuffer
			UNSAFE.copyMemory(source, offset, null, destinationAddress, size);
			length -= size;
			offset += size;
			destinationAddress += size;
		}
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.bytebuffer.ByteBuffer#free()
	 */
	public void free() {
		try {
			java.lang.reflect.Field cleanerField = byteBuffer.getClass().getDeclaredField("cleaner");
		    cleanerField.setAccessible(true);
		    // Cleaner can force freeing of native memory by clean method
		    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) cleanerField.get(byteBuffer);
		    cleaner.clean();
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	/**
	 * Gets the unsafe.
	 *
	 * @return the unsafe
	 */
	public static sun.misc.Unsafe getUnsafe() {
		try {
			java.lang.reflect.Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			unsafeField.setAccessible(true);
			return (sun.misc.Unsafe) unsafeField.get(null);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

}
