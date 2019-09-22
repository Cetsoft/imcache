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

import java.nio.BufferOverflowException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Class OffHeapByteBufferStore.
 */
public class OffHeapByteBufferStore implements OffHeapStore {

  /**
   * The buffers.
   */
  protected OffHeapByteBuffer[] buffers;
  /**
   * The available buffers.
   */
  protected BlockingQueue<Integer> availableBuffers;
  /**
   * The buffer size.
   */
  private volatile int bufferSize;
  /**
   * The current buffer.
   */
  private AtomicInteger currentBuffer = new AtomicInteger(0);

  /**
   * The buffer change lock.
   */
  private Lock bufferChangeLock = new ReentrantLock();

  /**
   * Instantiates a new off heap byte buffer store.
   *
   * @param capacity the capacity
   * @param bufferSize the buffer size
   */
  public OffHeapByteBufferStore(int capacity, int bufferSize) {
    this(capacity, bufferSize, OffHeapByteBuffer.DEFAULT_CONCURRENCY_LEVEL);
  }

  /**
   * Instantiates a new off heap byte buffer store.
   *
   * @param capacity the capacity
   * @param bufferSize the buffer size
   * @param concurrencyLevel the concurrency level
   */
  public OffHeapByteBufferStore(int capacity, int bufferSize, int concurrencyLevel) {
    this.bufferSize = bufferSize;
    buffers = new OffHeapByteBuffer[bufferSize];
    availableBuffers = new LinkedBlockingQueue<>(bufferSize);
    for (int i = 0; i < bufferSize; i++) {
      availableBuffers.add(i);
      buffers[i] = new OffHeapByteBuffer(i, capacity, concurrencyLevel);
    }
    currentBuffer.set(availableBuffers.poll());
  }


  public byte[] retrieve(Pointer pointer) {
    return pointer.getOffHeapByteBuffer().retrieve(pointer);
  }


  public byte[] remove(Pointer pointer) {
    return pointer.getOffHeapByteBuffer().remove(pointer);
  }


  public Pointer store(byte[] payload, long expiry) {
    while (true) {
      try {
        return currentBuffer().store(payload, expiry);
      } catch (BufferOverflowException exception) {
        try {
          return currentBuffer().store(payload, expiry);
        } catch (BufferOverflowException overflowException) {
          nextBuffer();
          return store(payload, expiry);
        }
      }
    }
  }

  /**
   * Gets and sets the next buffer.
   */
  protected void nextBuffer() {
    bufferChangeLock.lock();
    try {
      Integer currentBuffer = availableBuffers.poll();
      if (currentBuffer == null) {
        throw new BufferOverflowException();
      }
      this.currentBuffer.set(currentBuffer);
    } finally {
      bufferChangeLock.unlock();
    }
  }

  /**
   * Stores the payload to the available buffers except the given buffer.
   *
   * @param payload the payload
   * @param buffer the buffer
   * @return the pointer
   */
  protected Pointer store(final byte[] payload, final OffHeapByteBuffer buffer, final long expiry) {
    while (currentBuffer() == buffer) {
      nextBuffer();
    }
    return store(payload, expiry);
  }


  public Pointer update(final Pointer pointer, final byte[] payload, final long expiry) {
    try {
      return pointer.getOffHeapByteBuffer().update(pointer, payload, expiry);
    } catch (final BufferOverflowException exception) {
      return store(payload, expiry);
    }
  }


  public long dirtyMemory() {
    long dirtyMemory = 0;
    for (int i = 0; i < bufferSize; i++) {
      dirtyMemory += buffers[i].dirtyMemory();
    }
    return dirtyMemory;
  }


  public long usedMemory() {
    long usedMemory = 0;
    for (int i = 0; i < bufferSize; i++) {
      usedMemory += buffers[i].usedMemory();
    }
    return usedMemory;
  }


  public long freeMemory() {
    long freeMemory = 0;
    for (int i = 0; i < bufferSize; i++) {
      freeMemory += buffers[i].freeMemory();
    }
    return freeMemory;
  }

  /**
   * Free.
   */
  public void free() {
    for (int i = 0; i < bufferSize; i++) {
      free(i);
    }
  }

  /**
   * Free.
   *
   * @param bufferIndex the buffer index
   */
  public void free(int bufferIndex) {
    if (!availableBuffers.contains(bufferIndex)) {
      buffers[bufferIndex].free();
      availableBuffers.add(bufferIndex);
    }
  }

  /**
   * Returns the Current buffer.
   *
   * @return the off heap byte buffer
   */
  protected OffHeapByteBuffer currentBuffer() {
    return buffers[currentBuffer.get()];
  }

}
