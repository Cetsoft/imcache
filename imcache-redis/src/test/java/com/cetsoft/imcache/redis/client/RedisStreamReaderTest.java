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
* Date   : Aug 12, 2015
*/
package com.cetsoft.imcache.redis.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;


public class RedisStreamReaderTest {
	
	private static final int SIZE = 8192;

	RedisStreamReader reader;
	
	@Mock
	private InputStream inputStream;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		reader = spy(new RedisStreamReader(inputStream));
	}
	
	@Test
	public void read() throws IOException{
		final byte theByte = '*';
		final byte[] bytes = {theByte};
		setupInputStreamRead(bytes, SIZE);
		byte [] actualBytes = reader.read(1);
		assertArrayEquals(bytes, actualBytes);
	}
	
	@Test
	public void readNull() throws IOException{
		assertArrayEquals(null, reader.read(-1));
	}
	
	@Test
	public void readByte() throws IOException{
		final byte theByte = '*';
		final byte[] bytes = {theByte};
		setupInputStreamRead(bytes, SIZE);
		byte actualByte = reader.readByte();
		assertEquals(theByte, actualByte);
	}
	
	@Test
	public void readString() throws IOException{
		final byte[] bytes = {'O', 'K', '\r', '\n'};
		setupInputStreamRead(bytes, SIZE);
		String actualStatus = reader.readString();
		assertEquals("OK", actualStatus);
	}
	
	@Test
	public void readLongString() throws IOException{
		char theChar = '*';
		final byte[] bytes = new byte[10002];
		String expectedString = "";
		for (int i = 0; i < bytes.length-2; i++) {
			bytes[i] = (byte) theChar;
			expectedString += theChar;
		}
		bytes[bytes.length-2] = '\r';
		bytes[bytes.length-1] = '\n';
		setupInputStreamRead(bytes, SIZE);
		String actualString = reader.readString();
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void readLongStringWithoutLineFeed() throws IOException{
		char theChar = '*';
		final byte[] bytes = new byte[SIZE];
		String expectedString = "";
		for (int i = 0; i < bytes.length-1; i++) {
			bytes[i] = (byte) theChar;
			expectedString += theChar;
		}
		bytes[bytes.length-1] = '\r';
		setupInputStreamRead(bytes, SIZE);
		String actualString = reader.readString();
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void readPostiveInt() throws IOException{
		final byte[] bytes = {'1', '0', '\r', '\n'};
		setupInputStreamRead(bytes, 1);
		int actualInt = reader.readInt();
		assertEquals(10, actualInt);
	}
	
	@Test
	public void readNegativeInt() throws IOException{
		final byte[] bytes = {'-', '1', '0', '\r', '\n'};
		setupInputStreamRead(bytes, 1);
		int actualInt = reader.readInt();
		assertEquals(-10, actualInt);
	}
	
	protected void setupInputStreamRead(final byte[] bytes, int chunkSize) 
			throws IOException {
		Stubber stubber = null;
		for(int i=0; i<bytes.length/chunkSize + 1; i++){
			int length = (i + 1) * chunkSize < bytes.length? chunkSize : bytes.length - i * chunkSize;
			@SuppressWarnings("rawtypes")
			Answer answer = createAnswer(bytes, i * chunkSize, length);
			if(stubber == null){
				stubber = doAnswer(answer);
			}
			else{
				stubber = stubber.doAnswer(answer);
			}
		}
		stubber.when(inputStream).read((byte[]) any());
	}

	@SuppressWarnings("rawtypes")
	protected Answer createAnswer(final byte[] bytes, final int offset, final int length) {
		return new Answer() {
		    public Object answer(InvocationOnMock invocation) {
		        Object[] args = invocation.getArguments();
		        byte [] actualBytes = ((byte[])args[0]);
		        for (int i = offset; i < offset + length; i++) {
					actualBytes[i - offset] = bytes[i];
				}
		        return length;
		    }
		};
	}
	
}
