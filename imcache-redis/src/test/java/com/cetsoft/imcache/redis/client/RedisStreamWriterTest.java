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
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RedisStreamWriterTest {

	RedisStreamWriter writer;
	
	@Mock
	private OutputStream outputStream;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		writer = spy(new RedisStreamWriter(outputStream));
	}
	
	@Test
	public void flush() throws IOException{
		writer.flush();
		verify(outputStream).flush();
		verify(outputStream).write(writer.buffer, 0, writer.position);
		assertEquals(writer.position, 0);
	}
	
	@Test
	public void writeByte() throws IOException{
		byte theByte = '.';
		writer.position = writer.buffer.length;
		writer.write(theByte);
		verify(writer).flushBuffer();
		assertEquals(writer.position, 1);
		assertEquals(writer.buffer[0], theByte);
	}
	
	@Test
	public void writeInt() throws IOException{
		int value = -3;
		writer.position = writer.buffer.length;
		writer.write(value);
		verify(writer, times(2)).flushBuffer();
		assertEquals(writer.position, 0);
		assertEquals(writer.buffer[0], '-');
		assertEquals(writer.buffer[1], '3');
	}
	
	@Test
	public void writeNewLine() throws IOException{
		writer.writeNewLine();
		assertEquals(writer.buffer[0], '\r');
		assertEquals(writer.buffer[1], '\n');
	}
	
	@Test
	public void writeByteArray() throws IOException{
		byte [] bytes = { '1', '2', '3'};
		writer.position = writer.buffer.length-1;
		writer.write(bytes);
		verify(writer, times(2)).flushBuffer();
		assertEquals(bytes[0], writer.buffer[writer.buffer.length-1]);
		assertEquals(bytes[1], writer.buffer[0]);
		assertEquals(bytes[2], writer.buffer[1]);
	}
}
