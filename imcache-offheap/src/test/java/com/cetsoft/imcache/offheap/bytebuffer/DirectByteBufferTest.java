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
* Date   : May 22, 2014
*/
package com.cetsoft.imcache.offheap.bytebuffer;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.offheap.bytebuffer.DirectByteBuffer;

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
	
	@Test
	public void free(){
		int size = 1024*1024*2;
		byte[] bytes = new byte[size];
		random.nextBytes(bytes);
		buffer.free();
	}
}
