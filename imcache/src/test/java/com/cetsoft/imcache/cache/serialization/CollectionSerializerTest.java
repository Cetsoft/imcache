/*
* Copyright (C) 2013 Cetsoft, http://www.cetsoft.com
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
* Date   : Jun 2, 2014
*/
package com.cetsoft.imcache.cache.serialization;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CollectionItem;
import com.cetsoft.imcache.cache.util.SerializationUtils;
import com.cetsoft.imcache.serialization.CollectionSerializer;
import com.cetsoft.imcache.serialization.Serializer;

/**
 * The Class DiffCriteriaTest.
 */
public class CollectionSerializerTest {
	
	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Serialize and deserialize.
	 */
	@Test
	public void serializeAndDeserialize(){
		List<Integer> objects = new ArrayList<Integer>();
		objects.add(0);
		objects.add(1);
		objects.add(2);
		CollectionSerializer<Integer> collectionSerializer = new CollectionSerializer<Integer>(new Serializer<Integer>() {
			public byte[] serialize(Integer value) {
				return SerializationUtils.serializeInt(value);
			}
			public Integer deserialize(byte[] payload) {
				return SerializationUtils.deserializeInt(payload);
			}
		});
		CollectionItem<Integer> collectionItem = new CollectionItem<Integer>(objects);
		byte[] payload = collectionSerializer.serialize(collectionItem);
		CollectionItem<Integer> actualCollectionItem = collectionSerializer.deserialize(payload);
		assertTrue(actualCollectionItem.getValue().contains(0));
		assertTrue(actualCollectionItem.getValue().contains(1));
		assertTrue(actualCollectionItem.getValue().contains(2));
 	}
	
}
