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
* Date   : May 22, 2014
*/
package com.cetsoft.imcache.cache.offheap.bytebuffer;

import static org.junit.Assert.*;

import java.nio.BufferOverflowException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.cache.offheap.bytebuffer.OffHeapByteBufferException;
import com.cetsoft.imcache.cache.offheap.bytebuffer.Pointer;

/**
 * The Class DirectByteBufferTest.
 */
public class OffHeapByteBufferTest {
	
	/** The random. */
	Random random;

	/** The buffer. */
	OffHeapByteBuffer buffer = new OffHeapByteBuffer(0, 1024*1024*10);	
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		random = new Random();
	}
	
	/**
	 * Store.
	 */
	@Test
	public void store(){
		int size = 100;
		byte[] expectedBytes = new byte[size];
		random.nextBytes(expectedBytes);
		Pointer pointer = buffer.store(expectedBytes);
		byte[] actualBytes = buffer.retrieve(pointer);
		assertArrayEquals(expectedBytes, actualBytes);
	}
	
	/**
	 * Allocate.
	 */
	@Test(expected=BufferOverflowException.class)
	public void allocate(){
		int size = 11*1024*1024;
		byte[] expectedBytes = new byte[size];
		random.nextBytes(expectedBytes);
		buffer.allocate(expectedBytes);
	}
	
	/**
	 * Update.
	 */
	@Test
	public void update(){
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Pointer pointer = buffer.store(bytes);
		byte[] expectedBytes = new byte[size];
		random.nextBytes(expectedBytes);
		pointer = buffer.update(pointer, expectedBytes);
		byte[] actualBytes = buffer.retrieve(pointer);
		assertArrayEquals(expectedBytes, actualBytes);
	}
	
	/**
	 * Update greater ex payload.
	 */
	@Test
	public void updateGreaterExPayload(){
		int size = 100;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		Pointer pointer = buffer.store(bytes);
		byte[] expectedBytes = new byte[size+5];
		random.nextBytes(expectedBytes);
		pointer = buffer.update(pointer, expectedBytes);
		byte[] actualBytes = buffer.retrieve(pointer);
		assertArrayEquals(expectedBytes, actualBytes);
	}
	
	/**
	 * Header dirty.
	 */
	@Test(expected=OffHeapByteBufferException.class)
	public void headerDirty(){
		int size = 5;
		byte[] bytes = new byte[size];
		bytes[0] = -1;
		random.nextBytes(bytes);
		buffer.header(bytes);
	}
	
	/**
	 * Header free.
	 */
	@Test(expected=OffHeapByteBufferException.class)
	public void headerFree(){
		int size = 5;
		byte[] bytes = new byte[size];
		bytes[0] = 0;
		random.nextBytes(bytes);
		buffer.header(bytes);
	}
	
	/**
	 * Header used.
	 */
	@Test(expected=OffHeapByteBufferException.class)
	public void headerUsed(){
		int size = 5;
		byte[] bytes = new byte[size];
		bytes[0] = 1;
		random.nextBytes(bytes);
		buffer.header(bytes);
	}
	
	/**
	 * Removes the.
	 */
	@Test
	public void remove(){
		int size = 100;
		byte[] expectedBytes = new byte[size];
		random.nextBytes(expectedBytes);
		Pointer pointer = buffer.store(expectedBytes);
		byte[] actualBytes = buffer.remove(pointer);
		assertArrayEquals(expectedBytes, actualBytes);
	}

}
