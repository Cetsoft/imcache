/**
 * Copyright © 2013 Cetsoft. All rights reserved.
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
 */
package com.cetsoft.imcache.offheap.bytebuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.BufferOverflowException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

/**
 * The Class DirectByteBufferTest.
 */
public class OffHeapByteBufferStoreTest {

  /**
   * The random.
   */
  Random random;

  /**
   * The pointer.
   */
  @Mock
  Pointer pointer;

  /**
   * The queue.
   */
  @Mock
  BlockingQueue<Integer> queue;

  /**
   * The buffer.
   */
  @Mock
  OffHeapByteBuffer buffer;

  /**
   * The buffer store.
   */
  @Spy
  OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(1000, 1);

  /**
   * Setup.
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    random = new Random();
  }

  /**
   * Store.
   */
  @Test
  public void store() {
    int size = 100;
    final long expiry = System.currentTimeMillis();
    byte[] expectedBytes = new byte[size];
    random.nextBytes(expectedBytes);
    Pointer pointer = bufferStore.store(expectedBytes, expiry);
    byte[] actualBytes = bufferStore.retrieve(pointer);
    assertArrayEquals(expectedBytes, actualBytes);
  }

  /**
   * Store buffer over flow.
   */
  @Test
  public void storeBufferOverFlow() {
    int size = 100;
    final long expiry = System.currentTimeMillis();
    byte[] expectedBytes = new byte[size];
    random.nextBytes(expectedBytes);
    doReturn(buffer).when(bufferStore).currentBuffer();
    doThrow(new BufferOverflowException()).doReturn(pointer).when(buffer)
        .store(expectedBytes, expiry);
    Pointer actualPointer = bufferStore.store(expectedBytes, expiry);
    assertEquals(pointer, actualPointer);
    verify(bufferStore, times(2)).currentBuffer();
    verify(buffer, times(2)).store(expectedBytes, expiry);
  }

  /**
   * Store buffer over flow next buffer.
   */
  @Test
  public void storeBufferOverFlowNextBuffer() {
    int size = 100;
    final long expiry = System.currentTimeMillis();
    byte[] expectedBytes = new byte[size];
    random.nextBytes(expectedBytes);
    doReturn(buffer).when(bufferStore).currentBuffer();
    doNothing().when(bufferStore).nextBuffer();
    doThrow(new BufferOverflowException()).doThrow(new BufferOverflowException()).doReturn(pointer)
        .when(buffer)
        .store(expectedBytes, expiry);
    Pointer actualPointer = bufferStore.store(expectedBytes, expiry);
    assertEquals(pointer, actualPointer);
    verify(bufferStore, times(3)).currentBuffer();
    verify(buffer, times(3)).store(expectedBytes, expiry);
  }

  /**
   * Update.
   */
  @Test
  public void update() {
    int size = 100;
    final long expiry = System.currentTimeMillis();
    byte[] bytes = new byte[size];
    random.nextBytes(bytes);
    Pointer pointer = bufferStore.store(bytes, expiry);
    byte[] expectedBytes = new byte[size];
    random.nextBytes(expectedBytes);
    pointer = bufferStore.update(pointer, expectedBytes, expiry);
    byte[] actualBytes = bufferStore.retrieve(pointer);
    assertArrayEquals(expectedBytes, actualBytes);
  }

  /**
   * Update buffer over flow.
   */
  @Test
  public void updateBufferOverFlow() {
    int size = 100;
    final long expiry = System.currentTimeMillis();
    byte[] bytes = new byte[size];
    random.nextBytes(bytes);
    byte[] expectedBytes = new byte[size];
    random.nextBytes(expectedBytes);
    doReturn(buffer).when(pointer).getOffHeapByteBuffer();
    doThrow(new BufferOverflowException()).when(buffer).update(pointer, expectedBytes, expiry);
    pointer = bufferStore.update(pointer, expectedBytes, expiry);
    verify(bufferStore).store(expectedBytes, expiry);
  }

  /**
   * Next buffer buffer over flow.
   */
  @Test(expected = BufferOverflowException.class)
  public void nextBufferBufferOverFlow() {
    bufferStore.availableBuffers = queue;
    doReturn(null).when(queue).poll();
    bufferStore.nextBuffer();
    verify(queue).poll();
  }

  /**
   * Store with buffer.
   */
  @Test
  public void storeWithBuffer() {
    int size = 100;
    final long expiry = System.currentTimeMillis();
    byte[] bytes = new byte[size];
    random.nextBytes(bytes);
    doReturn(pointer).when(bufferStore).store(bytes, expiry);
    doReturn(buffer).doReturn(new OffHeapByteBuffer(0, 10)).when(bufferStore).currentBuffer();
    doNothing().when(bufferStore).nextBuffer();
    bufferStore.store(bytes, buffer, expiry);
    verify(bufferStore).store(bytes, expiry);
    verify(bufferStore).nextBuffer();
  }

  /**
   * Dirty memory.
   */
  @Test
  public void dirtyMemory() {
    long size = 100;
    bufferStore.buffers = new OffHeapByteBuffer[]{buffer};
    doReturn(size).when(buffer).dirtyMemory();
    long actualSize = bufferStore.dirtyMemory();
    assertEquals(size, actualSize);
  }

  /**
   * Free memory.
   */
  @Test
  public void freeMemory() {
    long size = 100;
    bufferStore.buffers = new OffHeapByteBuffer[]{buffer};
    doReturn(size).when(buffer).freeMemory();
    long actualSize = bufferStore.freeMemory();
    assertEquals(size, actualSize);
  }

  /**
   * Used memory.
   */
  @Test
  public void usedMemory() {
    long size = 100;
    bufferStore.buffers = new OffHeapByteBuffer[]{buffer};
    doReturn(size).when(buffer).usedMemory();
    long actualSize = bufferStore.usedMemory();
    assertEquals(size, actualSize);
  }

  /**
   * Free.
   */
  @Test
  public void free() {
    bufferStore.free();
    verify(bufferStore).free(anyInt());
    assertEquals(1, bufferStore.availableBuffers.size());
  }

  /**
   * Free with index.
   */
  @Test
  public void freeWithIndex() {
    bufferStore.buffers = new OffHeapByteBuffer[]{buffer};
    bufferStore.availableBuffers = queue;
    doReturn(true).when(queue).add(anyInt());
    doNothing().when(buffer).free();
    bufferStore.free(0);
    verify(buffer).free();
    verify(queue).add(anyInt());
  }

  @Test
  public void remove() {
    int size = 100;
    byte[] expectedBytes = new byte[size];
    random.nextBytes(expectedBytes);
    doReturn(buffer).when(pointer).getOffHeapByteBuffer();
    doReturn(expectedBytes).when(buffer).remove(pointer);
    byte[] actualBytes = bufferStore.remove(pointer);
    assertEquals(expectedBytes, actualBytes);
  }

}
