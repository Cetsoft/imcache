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
 * Date   : Aug 5, 2015
 */
package com.cetsoft.imcache.redis.client;

import java.io.IOException;
import java.io.InputStream;


/**
 * The Class RedisStreamReader.
 */
public class RedisStreamReader{

	/** The Constant BUFFER_SIZE. */
	public static final int BUFFER_SIZE = 8192;
	
	/** The input stream. */
	private InputStream inputStream;

	/**
	 * Instantiates a new redis stream reader.
	 *
	 * @param inputStream the input stream
	 */
	public RedisStreamReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * Reads byte.
	 *
	 * @return the byte
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte readByte() throws IOException {
		byte[] bytes = new byte[1];
		inputStream.read(bytes);
		return bytes[0];
	}
	
	/**
	 * Reads string.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readString() throws IOException {
		int bufferSize = BUFFER_SIZE;
		int noOfBytesRead = -1, newLength = -1;
		byte[] bytes = new byte[0];
		byte[] tempBytes = new byte[bufferSize];
		do{
			noOfBytesRead = inputStream.read(tempBytes);
			newLength = noOfBytesRead
				- (noOfBytesRead > 1 && tempBytes[noOfBytesRead-2] == RedisBytes.CARRIAGE_RETURN_BYTE ? 2 : 0)
				- (noOfBytesRead > 0 && tempBytes[noOfBytesRead-1] == RedisBytes.CARRIAGE_RETURN_BYTE ? 1 : 0);
			byte[] newBytes = new byte[newLength + bytes.length];
			System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
			System.arraycopy(tempBytes, 0, newBytes, bytes.length, newLength);
			bytes = newBytes;
		}while(noOfBytesRead != -1 && newLength == noOfBytesRead);
		return new String(bytes);
	}

	/**
	 * Read int.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int readInt() throws IOException {
		int length = 0;
		byte[] allIntBytes = new byte[13]; // maximum integer length + minus symbol + CR + LF = 13
		while((allIntBytes[length++] = readByte()) != RedisBytes.CARRIAGE_RETURN_BYTE);
		//Read Line feed byte
		readByte();
		int number = 0;
		boolean negative = allIntBytes.length > 0 && allIntBytes[0] == RedisBytes.DASH_BYTE;
		//We don't read last value if it's negative
		for (int i = length-2, j=0; i > (negative ? 0 : -1); i--, j++) {
			number += (int) (Math.pow(10, j) * (allIntBytes[i] - RedisBytes.ZERO_BYTE));
		}
		number = number * (negative ? -1 : 1);
		return number;
	}

	/**
	 * Reads bytes with given length.
	 *
	 * @param length the length
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] read(int length) throws IOException {
		if(length == -1){
			return null;
		}
		byte [] bytes = new byte[length];
		inputStream.read(bytes);
		//Remove Carriage Return and Line Feed
		inputStream.read(new byte[2]);
		return bytes;
	}

}
