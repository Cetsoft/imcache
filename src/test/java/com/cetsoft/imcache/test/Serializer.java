package com.cetsoft.imcache.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Serializer {
	public static byte[] serialize(Object object){
		byte[] objectBytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		try {
			ObjectOutput out = new ObjectOutputStream(bos);  
			out.writeObject(object);
			objectBytes = bos.toByteArray();
			out.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objectBytes;
	}
	
	@SuppressWarnings("unchecked")
	public static <C> C deserialize(byte [] bytes){
		Object object = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			ObjectInput in = new ObjectInputStream(bis);
			object = in.readObject(); 
			bis.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return (C)object;
	}
}
