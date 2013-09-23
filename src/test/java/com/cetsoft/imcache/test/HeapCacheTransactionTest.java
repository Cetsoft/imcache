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

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.cache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.cache.heap.tx.Transaction;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;

/**
 * The Class HeapCacheTransactionTest.
 */
public class HeapCacheTransactionTest {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String []args) {
		TransactionalHeapCache<Integer, Integer> cache = new TransactionalHeapCache<Integer, Integer>(new TransactionCommitter<Integer, Integer>() {
			public void doPut(Integer key, Integer value) {
				System.out.println("key["+key+"],"+"value["+value+"]");
			}
		}, new CacheLoader<Integer, Integer>() {public Integer load(Integer key) {return null;}}, 
		   new EvictionListener<Integer, Integer>() {public void onEviction(Integer key, Integer value) {}}, 
		   1000);
		Transaction transaction = CacheTransaction.get();
		transaction.begin();
		cache.put(3, 5);
		cache.put(10, 14);
		transaction.commit();
	}
}
