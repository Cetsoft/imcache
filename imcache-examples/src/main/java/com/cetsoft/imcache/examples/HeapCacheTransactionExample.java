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
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.examples;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.cache.heap.tx.Transaction;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.heap.tx.TransactionException;

/**
 * The Class HeapCacheTransactionTest.
 */
public class HeapCacheTransactionExample {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		Cache<Integer, Integer> cache = CacheBuilder.transactionalHeapCache()
				.transactionCommitter(new TransactionCommitter<Integer, Integer>() {
					int counter = 0;

					public void doPut(Integer key, Integer value) {
						if (counter < 3) {
							System.out.println("key[" + key + "]," + "value[" + value + "]");
							counter++;
						} else {
							throw new RuntimeException();
						}
					}
				}).build();
		Transaction transaction1 = CacheTransaction.get();
		transaction1.begin();
		try {
			cache.put(3, 5);
			cache.put(10, 14);
			transaction1.commit();
		} catch (TransactionException exception) {
			transaction1.rollback();
		} finally {
			transaction1.close();
		}
		System.out.println("Value for the key 3 is " + cache.get(3));
		System.out.println("Value for the key 10 is " + cache.get(10));
		Transaction transaction2 = CacheTransaction.get();
		transaction2.begin();
		try {
			cache.put(1, 10);
			cache.put(10, 13);
			transaction2.commit();
		} catch (TransactionException exception) {
			transaction2.rollback();
		} finally {
			transaction2.close();
		}
		System.out.println("Value for the key 1 is " + cache.get(1));
		System.out.println("Value for the key 10 is " + cache.get(10));
	}
}
