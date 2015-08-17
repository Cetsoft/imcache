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
* Date   : Aug 4, 2015
*/
package com.cetsoft.imcache.offheap.bytebuffer;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mock;

import com.cetsoft.imcache.offheap.bytebuffer.OffHeapByteBuffer;
import com.cetsoft.imcache.offheap.bytebuffer.Pointer;

public class PointerTest {
	
	@Mock
	OffHeapByteBuffer offHeapByteBuffer;
	
	@Test
	public void copy(){
		int position = 100;
		long accessTime = 1212;
		Pointer pointer = new Pointer(0, 0, null);
		Pointer pointerToCopy = new Pointer(0, 0, null);
		pointerToCopy.setAccessTime(accessTime);
		pointerToCopy.setPosition(position);
		pointerToCopy.setOffHeapByteBuffer(offHeapByteBuffer);
		pointer.copy(pointerToCopy);
		assertEquals(position, pointer.getPosition());
		assertEquals(accessTime, pointer.getAccessTime());
		assertEquals(offHeapByteBuffer, pointer.getOffHeapByteBuffer());
	}
}
