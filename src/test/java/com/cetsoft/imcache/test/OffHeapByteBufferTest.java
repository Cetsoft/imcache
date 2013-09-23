package com.cetsoft.imcache.test;

import java.io.Serializable;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.bytebuffer.Pointer;

public class OffHeapByteBufferTest {

	public static void main(String[] args) {
		int length = 5, x = 4, y = 3;
		OffHeapByteBuffer buffer = new OffHeapByteBuffer(0,1000);
		SimpleObject object = new SimpleObject(x, y);
		for (int i = 0; i < length; i++) {
			byte [] payload = Serializer.serialize(object);
			Pointer pointer = buffer.store(payload);
			SimpleObject simpleObject = Serializer.deserialize(buffer.retrieve(pointer));
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
	
}
