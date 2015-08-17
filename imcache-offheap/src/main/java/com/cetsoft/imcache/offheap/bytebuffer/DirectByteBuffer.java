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
 * The Class DirectByteBuffer.
 */
@SuppressWarnings("restriction")
public class DirectByteBuffer implements ByteBuffer {

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
	public DirectByteBuffer(int capacity) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.bytebuffer.ByteBuffer#get(int, byte[], int, int)
	 */
	public void get(int position, byte[] destination, int offset, int length) {
		copyToArray(getPosition(position), destination, ARRAY_BASE_OFFSET, offset, length);
	}

	/*
	 * (non-Javadoc)
	 * 
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
		// Calculate the distance from the beginning of the object up until a
		// destinationPoint
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
		// Calculate the distance from the beginning of the object up until a
		// sourcePoint
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

	/*
	 * (non-Javadoc)
	 * 
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
