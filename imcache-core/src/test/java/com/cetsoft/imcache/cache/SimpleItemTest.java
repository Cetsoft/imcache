package com.cetsoft.imcache.cache;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleItemTest {
	
	@Test
	public void simpleItem(){
		SimpleItem<Integer> item = new SimpleItem<Integer>(10);
		assertTrue(10 == item.getValue());
		item.setVersion(2);
		assertTrue(2 == item.getVersion());
		item.update(20);
		assertTrue(20 == item.getValue());
	}
}
