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
package com.cetsoft.imcache.serialization;

import java.util.ArrayList;
import java.util.List;

import com.cetsoft.imcache.cache.CollectionItem;
import com.cetsoft.imcache.cache.util.SerializationUtils;

/**
 * The Class CollectionSerializer.
 *
 * @param <V> the value type
 */
public class CollectionSerializer<V> implements Serializer<CollectionItem<V>> {

	/** The Constant OBJECT_SIZE. */
	private static final int OBJECT_SIZE = 68;

	/** The serializer. */
	private Serializer<V> serializer;

	/**
	 * Instantiates a new collection serializer.
	 *
	 * @param serializer the serializer
	 */
	public CollectionSerializer(Serializer<V> serializer) {
		this.serializer = serializer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cetsoft.imcache.serialization.Serializer#serialize(java.lang.Object)
	 */
	public byte[] serialize(CollectionItem<V> value) {
		List<Byte> bytes = new ArrayList<Byte>(value.getValue().size() * OBJECT_SIZE);
		for (V v : value.getValue()) {
			byte[] currentPayload = serializer.serialize(v);
			for (byte lengthByte : SerializationUtils.serializeInt(currentPayload.length)) {
				bytes.add(lengthByte);
			}
			for (int i = 0; i < currentPayload.length; i++) {
				bytes.add(currentPayload[i]);
			}
		}
		byte[] payload = new byte[bytes.size()];
		for (int i = 0; i < payload.length; i++) {
			payload[i] = bytes.get(i);
		}
		return payload;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cetsoft.imcache.serialization.Serializer#deserialize(byte[])
	 */
	public CollectionItem<V> deserialize(byte[] payload) {
		int pos = 0;
		List<V> values = new ArrayList<V>();
		while (pos < payload.length) {
			byte[] lengthBytes = new byte[4];
			System.arraycopy(payload, pos, lengthBytes, 0, 4);
			int length = SerializationUtils.deserializeInt(lengthBytes);
			pos += 4;
			byte[] bytes = new byte[length];
			System.arraycopy(payload, pos, bytes, 0, length);
			pos += length;
			V v = serializer.deserialize(bytes);
			values.add(v);
		}
		return new CollectionItem<V>(values);
	}

}