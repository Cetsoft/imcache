package com.cetsoft.imcache.serialization;

public interface Serializer<V>{
	byte[] serialize(V value);
	V deserialize(byte [] payload);
}
