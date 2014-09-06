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
* Date   : May 21, 2014
*/
package com.cetsoft.imcache.cache.heap;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.heap.tx.TransactionException;
import com.cetsoft.imcache.cache.search.IndexHandler;

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
