package com.cetsoft.imcache.cache.util;

public class SerializationUtils {
	
	public static byte[] serializeInt(int integer){
		byte[] ret = new byte[4];
	    ret[3] = (byte) (integer & 0xFF);   
	    ret[2] = (byte) ((integer >> 8) & 0xFF);   
	    ret[1] = (byte) ((integer >> 16) & 0xFF);   
	    ret[0] = (byte) ((integer >> 24) & 0xFF);
	    return ret;
	}
	
	public static int deserializeInt(byte[] integer){
	    int value = 0;
	    for (int i = 0; i < 4; i++) {
	        int shift = (4 - 1 - i) * 8;
	        value += (integer[i] & 0x000000FF) << shift;
	    }
	    return value;
	}
}
