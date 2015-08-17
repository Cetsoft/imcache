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
* Date   : Aug 17, 2015
*/
package com.cetsoft.imcache.redis.client;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Class RedisTransaction.
 */
public class RedisTransaction implements Transaction {

	/** The transaction lock. */
	Lock transactionLock = new ReentrantLock();
	
	/** The transaction condition. */
	Condition transactionCondition = transactionLock.newCondition();
	
	/** The thread in transaction. */
	AtomicReference<Thread> threadInTransaction = new AtomicReference<Thread>();

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.redis.client.Transaction#open()
	 */
	@Override
	public void open() {
		Thread currentThread = Thread.currentThread();
		try {
			if (threadInTransaction.get() == null){
				transactionLock.lock();
				try {
					if (threadInTransaction.get() == null) {
						threadInTransaction.set(currentThread);
						return;
					}
				} finally {
					transactionLock.unlock();
				}
				open();
			}
			else if(currentThread.equals(threadInTransaction.get())){
				return;
			}
			else {
				transactionLock.lock();
				try{
					while(threadInTransaction.get() != null){
						transactionCondition.await();
					}
				}finally{
					transactionLock.unlock();
				}
				open();
			}
		} catch (InterruptedException e) {
			//ignore interruption.
		}
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.redis.client.Transaction#close()
	 */
	@Override
	public void close() {
		transactionLock.lock();
		try {
			threadInTransaction.set(null);
			transactionCondition.signal();
		} finally {
			transactionLock.unlock();
		}
	}

}
