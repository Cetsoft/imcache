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
