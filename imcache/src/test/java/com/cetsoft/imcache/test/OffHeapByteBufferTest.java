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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.bytebuffer.Pointer;

/**
 * The Class OffHeapByteBufferTest.
 */
public class OffHeapByteBufferTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		int length = 5, x = 4, y = 3;
		OffHeapByteBuffer buffer = new OffHeapByteBuffer(0,1000);
		SimpleObject object = new SimpleObject(x, y);
		for (int i = 0; i < length; i++) {
			byte [] payload = serialize(object);
			Pointer pointer = buffer.store(payload);
			SimpleObject simpleObject = deserialize(buffer.retrieve(pointer));
			if(object.getX()!=simpleObject.getX()&&object.getY()!=simpleObject.getY()){
				System.err.println("Problem");
			}
		}
		Pointer pointer = buffer.update(new Pointer(115, buffer), new byte[200]);
		if(buffer.retrieve(pointer).length!=200){
			System.err.println("Problem");
		}
		buffer.retrieve(new Pointer(115, buffer));
	}

	/**
	 * Serialize.
	 *
	 * @param object the object
	 * @return the byte[]
	 */
	private static byte[] serialize(Object object){
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
	
	/**
	 * Deserialize.
	 *
	 * @param <C> the generic type
	 * @param bytes the bytes
	 * @return the c
	 */
	@SuppressWarnings("unchecked")
	private static <C> C deserialize(byte [] bytes){
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
