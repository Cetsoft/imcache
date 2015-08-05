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
package com.cetsoft.imcache.redis;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The Class RedisOutputStream.
 */
public class RedisOutputStream {

	/** The output stream. */
	private OutputStream outputStream;

	/**
	 * Instantiates a new redis output stream.
	 *
	 * @param outputStream the output stream
	 */
	public RedisOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * Flushes output stream.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void flush() throws IOException {
		outputStream.flush();
	}

}
