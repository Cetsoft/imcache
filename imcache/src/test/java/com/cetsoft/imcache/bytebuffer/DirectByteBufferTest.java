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
* Date   : May 22, 2014
*/
package com.cetsoft.imcache.bytebuffer;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * The Class DirectByteBufferTest.
 */
public class DirectByteBufferTest {
	
	/** The random. */
	Random random;

	/** The buffer. */
	DirectByteBuffer buffer = new DirectByteBuffer(1024*1024*10);
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		random = new Random();
	}

	/**
	 * Put.
	 */
	@Test
	public void put(){
		int size = 100;
		byte[] expectedBytes = new byte[size];
		random.nextBytes(expectedBytes);
		buffer.put(0, expectedBytes, 0, expectedBytes.length);
		byte[] actualBytes = new byte[size];
		buffer.get(0, actualBytes, 0, actualBytes.length);
		assertArrayEquals(expectedBytes, actualBytes);
	}
	
	/**
	 * Put length greater than threshold.
	 */
	@Test
	public void putLengthGreaterThanThreshold(){
		int size = 1024*1024*2;
		byte[] expectedBytes = new byte[size];
		random.nextBytes(expectedBytes);
		buffer.put(0, expectedBytes, 0, expectedBytes.length);
		byte[] actualBytes = new byte[size];
		buffer.get(0, actualBytes, 0, actualBytes.length);
		assertArrayEquals(expectedBytes, actualBytes);
	}
}
