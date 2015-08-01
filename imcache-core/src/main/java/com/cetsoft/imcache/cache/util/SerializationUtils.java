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
* Date   : Jan 16, 2014
*/
package com.cetsoft.imcache.cache.util;

/**
 * The Class SerializationUtils.
 */
public class SerializationUtils {

	/**
	 * Serialize integer.
	 *
	 * @param integer the integer
	 * @return the byte[]
	 */
	public static byte[] serializeInt(int integer) {
		byte[] ret = new byte[4];
		ret[3] = (byte) (integer & 0xFF);
		ret[2] = (byte) ((integer >> 8) & 0xFF);
		ret[1] = (byte) ((integer >> 16) & 0xFF);
		ret[0] = (byte) ((integer >> 24) & 0xFF);
		return ret;
	}

	/**
	 * Deserialize integer.
	 *
	 * @param integer the integer
	 * @return the int
	 */
	public static int deserializeInt(byte[] integer) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (integer[i] & 0x000000FF) << shift;
		}
		return value;
	}
}
