package com.cetsoft.imcache.offheap.bytebuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class OffHeapByteBufferTest {

	public static void main(String[] args) {
		int length = 5, x = 4, y = 3;
		OffHeapByteBuffer buffer = new OffHeapByteBuffer(1000);
		SimpleObject object = new SimpleObject(x, y);
		for (int i = 0; i < length; i++) {
			byte [] payload = serialize(object);
			Pointer pointer = buffer.store(payload);
			SimpleObject simpleObject = deseralize(buffer.retrieve(pointer));
			if(object.getX()!=simpleObject.getX()&&object.getY()!=simpleObject.getY()){
				System.err.println("Problem");
			}
		}
		Pointer pointer = buffer.update(new Pointer(115, buffer), new byte[200]);
		if(buffer.retrieve(pointer).length!=200){
			System.err.println("Problem");
		}
		buffer.retrieve(new Pointer(115, buffer));
		System.out.println();
	}

	private static class SimpleObject implements Serializable{
		private static final long serialVersionUID = -4857889076136732189L;

		private int x, y;

		public SimpleObject(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
	
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
	public static <C> C deseralize(byte [] bytes){
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
