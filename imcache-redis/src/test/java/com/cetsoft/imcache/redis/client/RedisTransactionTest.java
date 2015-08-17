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

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedisTransactionTest {
	
	final RedisTransaction transaction = spy(new RedisTransaction());
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void openClose(){
		Thread thread = Thread.currentThread();
		transaction.open();
		assertEquals(transaction.threadInTransaction.get(), thread);
		transaction.close();
		assertEquals(transaction.threadInTransaction.get(), null);
	}
	
	@Test
	public void openCloseSameThread(){
		Thread thread = Thread.currentThread();
		transaction.open();
		transaction.open();
		assertEquals(transaction.threadInTransaction.get(), thread);
		transaction.close();
		assertEquals(transaction.threadInTransaction.get(), null);
	}
	
	@Test
	public void openCloseAnotherThreadInTransaction(){
		int noOfThreads = 5;
		final CountDownLatch countDownLatch = new CountDownLatch(noOfThreads);
		for (int i = 0; i < noOfThreads; i++) {
			final int multiplier = i*10;
			new Thread(new Runnable() {
				@Override
				public void run() {
					transaction.open();
					try {
						Thread.sleep(multiplier);
					} catch (InterruptedException e) {}
					transaction.close();
					countDownLatch.countDown();
				}
			}).start();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
