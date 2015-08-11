package com.cetsoft.imcache.redis.client;

import org.junit.Test;

import static org.junit.Assert.*;


public class RedisCommandsTest {
	
	@Test
	public void getBytes(){
		byte[] expectedGetBytes = new byte[]{71, 69, 84};
		byte[] expectedDBSizeBytes = new byte[]{68, 66, 83, 73, 90, 69};
		assertArrayEquals(expectedGetBytes, RedisCommands.GET.getBytes());
		assertArrayEquals(expectedDBSizeBytes, RedisCommands.DBSIZE.getBytes());
	}
}
