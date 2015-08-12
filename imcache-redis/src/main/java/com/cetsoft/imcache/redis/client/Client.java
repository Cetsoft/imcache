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
 * The Interface Client provides redis commands. Methods in this 
 * interface have corresponding redis command with the same name. 
 * This class performs operations by the help of Connection and
 * RedisInputStream and RedisOutputStream.
 */
public interface Client {
	
	/**
	 * Ping the server. This command is often used to test if a connection
	 * is still alive.
	 * @throws ConnectionException 
	 * @throws IOException 
	 */
	void ping() throws ConnectionException, IOException;

	/**
	 * Set key to hold the value. If key already holds a value, 
	 * it is overwritten.
	 *
	 * @param key the key
	 * @param value the value
	 * @throws IOException 
	 * @throws ConnectionException 
	 */
	void set(byte[] key, byte[] value) throws ConnectionException, IOException;

	/**
	 * Get the value of key. If the key does not exist the special
	 * value null is returned. 
	 *
	 * @param key the key
	 * @return the byte[]
	 * @throws IOException 
	 * @throws ConnectionException 
	 */
	byte[] get(byte[] key) throws ConnectionException, IOException;

	/**
	 * Set a timeout on key. After the timeout has expired, the key will 
	 * automatically be deleted. 
	 *
	 * @param key the key
	 * @return the byte[]
	 * @throws IOException 
	 * @throws ConnectionException 
	 */
	byte[] expire(byte[] key) throws ConnectionException, IOException;

	/**
	 * Delete all the keys of the currently selected DB.
	 * @throws IOException 
	 * @throws ConnectionException 
	 */
	void flushdb() throws ConnectionException, IOException;
	
	/**
	 * Return the number of keys.
	 *
	 * @return the int
	 * @throws IOException 
	 * @throws ConnectionException 
	 */
	int dbsize() throws ConnectionException, IOException;
	
}
