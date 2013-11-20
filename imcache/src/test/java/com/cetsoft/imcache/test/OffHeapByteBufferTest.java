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

import java.io.Serializable;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.bytebuffer.Pointer;
import com.cetsoft.imcache.examples.Serializer;

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

	/**
	 * The Class SimpleObject.
	 */
	public static class SimpleObject implements Serializable{
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -4857889076136732189L;

		/** The y. */
		private int x, y;

		/**
		 * Instantiates a new simple object.
		 *
		 * @param x the x
		 * @param y the y
		 */
		public SimpleObject(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Gets the x.
		 *
		 * @return the x
		 */
		public int getX() {
			return x;
		}

		/**
		 * Gets the y.
		 *
		 * @return the y
		 */
		public int getY() {
			return y;
		}
	}
	
}
