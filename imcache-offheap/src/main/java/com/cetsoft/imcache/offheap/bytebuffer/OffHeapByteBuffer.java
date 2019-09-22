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

import com.cetsoft.imcache.concurrent.StripedReadWriteLock;
import java.nio.BufferOverflowException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Class OffHeapByteBuffer.
 */
public class OffHeapByteBuffer implements OffHeapStore {

  /**
   * The Constant DEFAULT_CONCURRENCY_LEVEL.
   */
  public final static int DEFAULT_CONCURRENCY_LEVEL = 4;
  /**
   * The Constant POINTER_SIZE.
   */
  private final static int POINTER_SIZE = 5;
  /**
   * The Constant USED.
   */
  private final static byte USED = 1;
  /**
   * The Constant FREE.
   */
  private final static byte FREE = 0;
  /**
   * The Constant DIRTY.
   */
  private final static byte DIRTY = -1;
  /**
   * The direct byte buffer.
   */
  private final DirectByteBuffer directByteBuffer;
  /**
   * The read write lock.
   */
  private final StripedReadWriteLock readWriteLock;
  /**
   * The offset.
   */
  private final AtomicInteger offset = new AtomicInteger(0);
  /**
   * The used memory.
   */
  private final AtomicInteger usedMemory = new AtomicInteger(0);
  /**
   * The dirty memory.
   */
  private final AtomicInteger dirtyMemory = new AtomicInteger(0);
  /**
   * The index.
   */
  private volatile int index;
  /**
   * The capacity.
   */
  private volatile long capacity;

  /**
   * Instantiates a new off heap byte buffer.
   *
   * @param index the index
   * @param capacity the capacity
   */
  public OffHeapByteBuffer(int index, int capacity) {
    this(index, capacity, DEFAULT_CONCURRENCY_LEVEL);
  }

  /**
   * Instantiates a new off heap byte buffer.
   *
   * @param index the index
   * @param capacity the capacity
   * @param concurrencyLevel the concurrency level
   */
  public OffHeapByteBuffer(int index, int capacity, int concurrencyLevel) {
    this.index = index;
    this.capacity = capacity;
    directByteBuffer = new DirectByteBuffer(capacity);
    readWriteLock = new StripedReadWriteLock(concurrencyLevel);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.bytebuffer.OffHeapStore#retrieve(com.cetsoft.imcache
   * .bytebuffer.Pointer)
   */
  public byte[] retrieve(final Pointer pointer) {
    readWriteLock.readLock(pointer.getPosition());
    try {
      final byte[] header = new byte[POINTER_SIZE];
      directByteBuffer.get(pointer.getPosition(), header, 0, POINTER_SIZE);
      final int length = header(header);
      final byte[] payload = new byte[length];
      directByteBuffer.get(pointer.getPosition() + POINTER_SIZE, payload, 0, length);
      return payload;
    } finally {
      readWriteLock.readUnlock(pointer.getPosition());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.bytebuffer.OffHeapStore#remove(com.cetsoft.imcache
   * .bytebuffer.Pointer)
   */
  public byte[] remove(Pointer pointer) {
    readWriteLock.writeLock(pointer.getPosition());
    try {
      final byte[] payload = retrieve(pointer);
      dirtyMemory.addAndGet(payload.length + POINTER_SIZE);
      markAsDirty(pointer.getPosition());
      return payload;
    } finally {
      readWriteLock.writeUnlock(pointer.getPosition());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.bytebuffer.OffHeapStore#store(byte[])
   */
  public Pointer store(final byte[] payload, final long expiry) {
    final Allocation allocation = allocate(payload);
    return store(allocation, payload, expiry);
  }

  /**
   * Stores the payload by the help of allocation.
   *
   * @param allocation the allocation
   * @param payload the payload
   * @return the pointer
   */
  public Pointer store(final Allocation allocation, byte[] payload, final long expiry) {
    usedMemory.addAndGet(allocation.getLength());
    final Pointer pointer = new Pointer(allocation.getOffset(), expiry, this);
    final byte[] header = header(payload.length);
    directByteBuffer.put(allocation.getOffset(), header, 0, POINTER_SIZE);
    directByteBuffer.put(allocation.getOffset() + POINTER_SIZE, payload, 0,
        allocation.getLength() - POINTER_SIZE);
    return pointer;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * com.cetsoft.imcache.bytebuffer.OffHeapStore#update(com.cetsoft.imcache
   * .bytebuffer.Pointer, byte[])
   */
  public Pointer update(final Pointer pointer, final byte[] payload, final long expiry) {
    readWriteLock.writeLock(pointer.getPosition());
    try {
      final byte[] exPayload = retrieve(pointer);
      if (exPayload.length >= payload.length) {
        // Note that updated payload should be always less than
        // exPayload,
        // otherwise new location is allocated.
        dirtyMemory.addAndGet(exPayload.length - payload.length);
        final Allocation allocation = new Allocation(pointer.getPosition(),
            payload.length + POINTER_SIZE);
        return store(allocation, payload, expiry);
      } else {
        dirtyMemory.addAndGet(exPayload.length + POINTER_SIZE);
        markAsDirty(pointer.getPosition());
        return store(payload, expiry);
      }
    } finally {
      readWriteLock.writeUnlock(pointer.getPosition());
    }
  }

  /**
   * Calculates the header bytes from the length.
   *
   * @param length the length
   * @return the byte[]
   */
  protected byte[] header(int length) {
    final byte[] header = new byte[5];
    header[0] = USED;
    header[1] = (byte) ((length >>> 24) & 0xFF);
    header[2] = (byte) ((length >>> 16) & 0xFF);
    header[3] = (byte) ((length >>> 8) & 0xFF);
    header[4] = (byte) ((length >>> 0) & 0xFF);
    return header;
  }

  /**
   * Calculates the header length from the bytes.
   *
   * @param header the header
   * @return the int
   */
  protected int header(byte[] header) {
    if (header[0] == DIRTY) {
      throw new OffHeapByteBufferException("Object is dirty!");
    } else if (header[0] == FREE) {
      throw new OffHeapByteBufferException("Object is free!");
    } else if (header[0] != USED) {
      throw new OffHeapByteBufferException("Wrong header!");
    }
    int length = ((header[1]) << 24) | ((header[2] & 0xff) << 16) | ((header[3] & 0xff) << 8)
        | ((header[4] & 0xff));
    return length;
  }

  /**
   * Mark as dirty.
   *
   * @param offset the offset
   */
  protected void markAsDirty(int offset) {
    final byte[] dirtyMark = {DIRTY};
    directByteBuffer.put(offset, dirtyMark, 0, 1);
  }

  /**
   * Allocates memory for the payload.
   *
   * @param payload the payload
   * @return the allocation
   */
  protected Allocation allocate(byte[] payload) {
    final int payloadLength = payload.length + POINTER_SIZE;
    final int allocationOffset = offset.addAndGet(payloadLength);
    if (this.capacity < allocationOffset) {
      throw new BufferOverflowException();
    }
    return new Allocation(allocationOffset - payloadLength, payloadLength);
  }

  /**
   * Frees the buffer.
   */
  public void free() {
    directByteBuffer.free();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.bytebuffer.OffHeapStore#dirtyMemory()
   */
  public long dirtyMemory() {
    return dirtyMemory.get();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.bytebuffer.OffHeapStore#usedMemory()
   */
  public long usedMemory() {
    return usedMemory.get();
  }

  /*
   * (non-Javadoc)
   *
   * @see com.cetsoft.imcache.bytebuffer.OffHeapStore#freeMemory()
   */
  public long freeMemory() {
    return capacity - (dirtyMemory() + usedMemory());
  }

  /**
   * Gets the index.
   *
   * @return the index
   */
  public int getIndex() {
    return index;
  }

  /**
   * The Class Allocation.
   */
  private static class Allocation {

    /**
     * The offset.
     */
    private final int offset;

    /**
     * The length.
     */
    private final int length;

    /**
     * Instantiates a new allocation.
     *
     * @param offset the offset
     * @param length the length
     */
    public Allocation(int offset, int length) {
      this.offset = offset;
      this.length = length;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    public int getOffset() {
      return offset;
    }

    /**
     * Gets the length.
     *
     * @return the length
     */
    public int getLength() {
      return length;
    }
  }
}
