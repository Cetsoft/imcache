package com.cetsoft.imcache.examples;

import org.junit.Test;
import static org.junit.Assert.*;


public class SerializerTest {
	
	@Test
	public void serializeDeserialize(){
		Integer item = new Integer(1);
		byte[] serializedItem = Serializer.serialize(item);
		Integer deserializeItem = Serializer.deserialize(serializedItem);
		assertEquals(item, deserializeItem);
	}
}
