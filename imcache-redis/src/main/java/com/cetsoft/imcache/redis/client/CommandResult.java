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
* Date   : Aug 7, 2015
*/
package com.cetsoft.imcache.redis.client;

import java.io.IOException;

/**
 * The Interface CommandResult returns the byte result of an executed command.
 */
public interface CommandResult {
	
	/**
	 * Gets returned bytes of the result of an executed command.
	 *
	 * @return the byte[]
	 * @throws ConnectionException the connection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	byte[] getBytes()  throws ConnectionException, IOException;
	
	/**
	 * Gets the status of an executed command.
	 *
	 * @return the status
	 * @throws ConnectionException the connection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	String getStatus()  throws ConnectionException, IOException;
	
	/**
	 * Gets the int value returned by executed command.
	 *
	 * @return the int
	 * @throws ConnectionException the connection exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	int getInt()  throws ConnectionException, IOException;
	
}
