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
* Date   : May 21, 2014
*/
package com.cetsoft.imcache.heap;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.heap.TransactionalHeapCache;
import com.cetsoft.imcache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.heap.tx.TransactionException;

/**
 * The Class TransactionalHeapCacheTest.
 */
public class TransactionalHeapCacheTest {

	/** The cache loader. */
	@Mock
	CacheLoader<Object, Object> cacheLoader;

	/** The eviction listener. */
	@Mock
	EvictionListener<Object, Object> evictionListener;

	/** The index handler. */
	@Mock
	IndexHandler<Object, Object> indexHandler;

	/** The committer. */
	@Mock
	TransactionCommitter<Object, Object> committer;

	/** The cache. */
	TransactionalHeapCache<Object, Object> cache;

	/** The map. */
	@Mock
	Map<Object, Object> map;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		cache = spy(new TransactionalHeapCache<Object, Object>(committer, cacheLoader, evictionListener, indexHandler,
				1000));
		cache.cache = map;
	}

	/**
	 * Put success.
	 */
	@Test
	public void putSuccess() {
		doReturn("1").when(cache.cache).get(1);
		doNothing().when(committer).doPut(any(), any());
		CacheTransaction.get().begin();
		try {
			cache.put(1, "2");
			CacheTransaction.get().commit();
		} catch (TransactionException exception) {
			CacheTransaction.get().rollback();
		} finally {
			CacheTransaction.get().close();
		}
		verify(committer).doPut(any(), any());
	}

	/**
	 * Put fail.
	 */
	@Test
	public void putFail() {
		doReturn("1").when(cache.cache).get(1);
		doThrow(new TransactionException(new Exception())).when(committer).doPut(any(), any());
		CacheTransaction.get().begin();
		try {
			cache.put(1, "2");
			CacheTransaction.get().commit();
		} catch (TransactionException exception) {
			CacheTransaction.get().rollback();
		} finally {
			CacheTransaction.get().close();
		}
		verify(cache.cache, times(2)).put(any(), any());
	}
}
