/*
* Copyright (C) 2013 Yusuf Aytas, http://www.yusufaytas.com
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
	public static byte[] serializeInt(int integer){
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
	public static int deserializeInt(byte[] integer){
	    int value = 0;
	    for (int i = 0; i < 4; i++) {
	        int shift = (4 - 1 - i) * 8;
	        value += (integer[i] & 0x000000FF) << shift;
	    }
	    return value;
	}
}
