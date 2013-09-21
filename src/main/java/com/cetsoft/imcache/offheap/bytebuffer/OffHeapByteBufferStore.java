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
* Date   : Sep 22, 2013
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
public class OffHeapByteBufferStore implements OffHeapStore{
	
	/** The buffer size. */
	private volatile int bufferSize;
	
	/** The buffers. */
	private OffHeapByteBuffer[] buffers;
	
	/** The available buffers. */
	private BlockingQueue<Integer> availableBuffers;
	
	/** The current buffer. */
	private AtomicInteger currentBuffer = new AtomicInteger(0);
	
	/** The buffer change lock. */
	private Lock bufferChangeLock = new ReentrantLock();
	
	/** The Constant DEFAULT_BUFFER_CLEANER_PERIOD. */
	public final static long DEFAULT_BUFFER_CLEANER_PERIOD = 10*60*1000;// 10 minutes
	
	/** The Constant DEFAULT_BUFFER_CLEANER_THRESHOLD. */
	public final static float DEFAULT_BUFFER_CLEANER_THRESHOLD = 0.5f;
	
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
		availableBuffers = new LinkedBlockingQueue<Integer>(bufferSize);
		for (int i = 0; i < bufferSize; i++) {
			availableBuffers.add(i);
			buffers[0] = new OffHeapByteBuffer(capacity, concurrencyLevel);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.offheap.bytebuffer.OffHeapStore#retrieve(com.cetsoft.imcache.offheap.bytebuffer.Pointer)
	 */
	public byte[] retrieve(Pointer pointer) {
		return pointer.getOffHeapByteBuffer().retrieve(pointer);
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.offheap.bytebuffer.OffHeapStore#remove(com.cetsoft.imcache.offheap.bytebuffer.Pointer)
	 */
	public byte[] remove(Pointer pointer) {
		return pointer.getOffHeapByteBuffer().remove(pointer);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.offheap.bytebuffer.OffHeapStore#store(byte[])
	 */
	public Pointer store(byte[] payload) {
		while(true){
			try{
				return currentBuffer().store(payload);
			}catch(BufferOverflowException exception){
				bufferChangeLock.lock();
				try{
					Integer currentBuffer = availableBuffers.poll();
					if(currentBuffer==null){
						throw new BufferOverflowException();
					}
					this.currentBuffer.set(currentBuffer);
					return store(payload);
				}finally{
					bufferChangeLock.unlock();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.offheap.bytebuffer.OffHeapStore#update(com.cetsoft.imcache.offheap.bytebuffer.Pointer, byte[])
	 */
	public Pointer update(Pointer pointer, byte[] payload) {
		try{
			return pointer.getOffHeapByteBuffer().update(pointer, payload);
		}catch(BufferOverflowException exception){
			return store(payload);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.offheap.bytebuffer.OffHeapStore#dirtyMemory()
	 */
	public long dirtyMemory() {
		long dirtyMemory = 0;
		for (int i = 0; i < bufferSize; i++) {
			dirtyMemory += buffers[i].dirtyMemory();
		}
		return dirtyMemory;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.offheap.bytebuffer.OffHeapStore#usedMemory()
	 */
	public long usedMemory() {
		long usedMemory = 0;
		for (int i = 0; i < bufferSize; i++) {
			usedMemory += buffers[i].usedMemory();
		}
		return usedMemory;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.offheap.bytebuffer.OffHeapStore#freeMemory()
	 */
	public long freeMemory() {
		long freeMemory = 0;
		for (int i = 0; i < bufferSize; i++) {
			freeMemory += buffers[i].freeMemory();
		}
		return freeMemory;
	}
	
	/**
	 * Returns the Current buffer.
	 *
	 * @return the off heap byte buffer
	 */
	protected OffHeapByteBuffer currentBuffer(){
		return buffers[currentBuffer.get()];
	}
	
	/**
	 * Frees the specified buffer by bufferIndex.
	 *
	 * @param bufferIndex the buffer index
	 */
	public void freeBuffer(int bufferIndex){
		buffers[bufferIndex].free();
		availableBuffers.add(bufferIndex);
	}
	
	public void freeAll(){
		for (int i = 0; i < bufferSize; i++) {
			freeBuffer(i);
		}
	}
	
}
