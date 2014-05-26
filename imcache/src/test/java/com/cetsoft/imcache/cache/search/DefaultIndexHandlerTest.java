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
 * Date   : May 25, 2014
 */
package com.cetsoft.imcache.cache.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.search.criteria.AndCriteria;
import com.cetsoft.imcache.cache.search.criteria.ArithmeticCriteria;
import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.criteria.DiffCriteria;
import com.cetsoft.imcache.cache.search.criteria.OrCriteria;
import com.cetsoft.imcache.cache.search.index.CacheIndex;
import com.cetsoft.imcache.cache.search.index.IndexNotFoundException;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.cache.search.index.NonUniqueHashIndex;
import com.cetsoft.imcache.cache.search.index.RangeIndex;
import com.cetsoft.imcache.cache.search.index.UniqueHashIndex;

/**
 * The Class DefaultIndexHandlerTest.
 */
public class DefaultIndexHandlerTest {

	/** The query. */
	@Mock
	Query query;

	/** The and criteria. */
	@Mock
	AndCriteria andCriteria;

	/** The or criteria. */
	@Mock
	OrCriteria orCriteria;

	/** The diff criteria. */
	@Mock
	DiffCriteria diffCriteria;

	/** The arithmetic criteria. */
	@Mock
	ArithmeticCriteria arithmeticCriteria;

	/** The criteria. */
	@Mock
	Criteria criteria;

	/** The cache. */
	@Mock
	Cache<Object, Object> cache;

	/** The indexes. */
	@Mock
	Map<String, CacheIndex> indexes;

	/** The cache index. */
	@Mock
	CacheIndex cacheIndex;

	/** The handler. */
	DefaultIndexHandler<Object, Object> handler;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		handler = spy(new DefaultIndexHandler<Object, Object>());
		handler.indexes = indexes;
	}

	/**
	 * Adds the index unique hash.
	 */
	@Test
	public void addIndexUniqueHash() {
		doReturn(null).when(indexes).put(anyString(), any(CacheIndex.class));
		handler.addIndex("a", IndexType.UNIQUE_HASH);
		verify(indexes).put(anyString(), any(UniqueHashIndex.class));
	}

	/**
	 * Adds the index non unique hash.
	 */
	@Test
	public void addIndexNonUniqueHash() {
		doReturn(null).when(indexes).put(anyString(), any(CacheIndex.class));
		handler.addIndex("a", IndexType.NON_UNIQUE_HASH);
		verify(indexes).put(anyString(), any(NonUniqueHashIndex.class));
	}

	/**
	 * Adds the index range.
	 */
	@Test
	public void addIndexRange() {
		doReturn(null).when(indexes).put(anyString(), any(CacheIndex.class));
		handler.addIndex("a", IndexType.RANGE_INDEX);
		verify(indexes).put(anyString(), any(RangeIndex.class));
	}

	/**
	 * Adds the.
	 */
	@Test
	public void add() {
		String attributeName = "name";
		Object object = new Object();
		Set<String> keySet = new HashSet<String>();
		keySet.add(attributeName);
		doReturn(keySet).when(indexes).keySet();
		doReturn(object).when(handler).getIndexedKey(attributeName, object);
		doReturn(cacheIndex).when(indexes).get(attributeName);
		doNothing().when(cacheIndex).put(object, object);
		handler.add(object, object);
		verify(cacheIndex).put(object, object);
		verify(indexes).get(attributeName);
		verify(handler).getIndexedKey(attributeName, object);
	}

	/**
	 * Adds the indexed key null.
	 */
	@Test(expected = NullPointerException.class)
	public void addIndexedKeyNull() {
		String attributeName = "name";
		Object object = new Object();
		Set<String> keySet = new HashSet<String>();
		keySet.add(attributeName);
		doReturn(keySet).when(indexes).keySet();
		doReturn(null).when(handler).getIndexedKey(attributeName, object);
		handler.add(object, object);
	}

	/**
	 * Removes the.
	 */
	@Test
	public void remove() {
		String attributeName = "name";
		Object object = new Object();
		Set<String> keySet = new HashSet<String>();
		keySet.add(attributeName);
		doReturn(keySet).when(indexes).keySet();
		doReturn(object).when(handler).getIndexedKey(attributeName, object);
		doReturn(cacheIndex).when(indexes).get(attributeName);
		doNothing().when(cacheIndex).remove(object, object);
		handler.remove(object, object);
		verify(cacheIndex).remove(object, object);
		verify(indexes).get(attributeName);
		verify(handler).getIndexedKey(attributeName, object);
	}

	/**
	 * Removes the indexed key null.
	 */
	@Test(expected = NullPointerException.class)
	public void removeIndexedKeyNull() {
		String attributeName = "name";
		Object object = new Object();
		Set<String> keySet = new HashSet<String>();
		keySet.add(attributeName);
		doReturn(keySet).when(indexes).keySet();
		doReturn(null).when(handler).getIndexedKey(attributeName, object);
		handler.remove(object, object);
	}

	/**
	 * Clear.
	 */
	@Test
	public void clear() {
		doNothing().when(indexes).clear();
		handler.clear();
		verify(indexes).clear();
	}

	/**
	 * Execute.
	 */
	@Test
	public void execute() {
		doReturn(criteria).when(query).getCriteria();
		doReturn(new ArrayList<Object>()).when(handler).execute(criteria);
		handler.execute(query);
		verify(handler).execute(criteria);
	}

	/**
	 * Execute for arithmetic.
	 */
	@Test
	public void executeForArithmetic() {
		doReturn(new ArrayList<Object>()).when(handler).executeArithmetic(arithmeticCriteria);
		handler.execute(arithmeticCriteria);
		verify(handler).executeArithmetic(arithmeticCriteria);
	}

	/**
	 * Execute for and.
	 */
	@Test
	public void executeForAnd() {
		doReturn(new ArrayList<Object>()).when(handler).executeAnd(andCriteria);
		handler.execute(andCriteria);
		verify(handler).executeAnd(andCriteria);
	}

	/**
	 * Execute for or.
	 */
	@Test
	public void executeForOr() {
		doReturn(new ArrayList<Object>()).when(handler).executeOr(orCriteria);
		handler.execute(orCriteria);
		verify(handler).executeOr(orCriteria);
	}

	/**
	 * Execute for diff.
	 */
	@Test
	public void executeForDiff() {
		doReturn(new ArrayList<Object>()).when(handler).executeDiff(diffCriteria);
		handler.execute(diffCriteria);
		verify(handler).executeDiff(diffCriteria);
	}

	/**
	 * Execute arithmetic.
	 */
	@Test
	public void executeArithmetic() {
		String attributeName = "name";
		List<Object> objects = new ArrayList<Object>();
		doReturn(attributeName).when(arithmeticCriteria).getAttributeName();
		doReturn(cacheIndex).when(indexes).get(attributeName);
		doReturn(objects).when(arithmeticCriteria).meets(cacheIndex);
		List<Object> actualObjects = handler.executeArithmetic(arithmeticCriteria);
		assertEquals(objects, actualObjects);
	}

	/**
	 * Execute arithmetic throws index not found exception.
	 */
	@Test(expected = IndexNotFoundException.class)
	public void executeArithmeticThrowsIndexNotFoundException() {
		String attributeName = "name";
		doReturn(attributeName).when(arithmeticCriteria).getAttributeName();
		doReturn(null).when(indexes).get(attributeName);
		handler.executeArithmetic(arithmeticCriteria);
	}

	/**
	 * Execute and.
	 */
	@Test
	public void executeAnd() {
		Object object1 = new Object();
		Object object2 = new Object();
		Object object3 = new Object();
		List<Object> objectList1 = new ArrayList<Object>();
		List<Object> objectList2 = new ArrayList<Object>();
		objectList1.add(object1);
		objectList1.add(object2);
		objectList2.add(object2);
		objectList2.add(object3);
		doReturn(new Criteria[] { criteria, criteria }).when(andCriteria).getCriterias();
		doReturn(objectList1).doReturn(objectList2).when(handler).execute(criteria);
		List<Object> actualObjects = handler.executeAnd(andCriteria);
		assertEquals(object2, actualObjects.get(0));
	}

	/**
	 * Execute diff.
	 */
	@Test
	public void executeDiff() {
		Object object1 = new Object();
		Object object2 = new Object();
		Object object3 = new Object();
		List<Object> objectList1 = new ArrayList<Object>();
		List<Object> objectList2 = new ArrayList<Object>();
		objectList1.add(object1);
		objectList1.add(object2);
		objectList2.add(object2);
		objectList2.add(object3);
		doReturn(criteria).when(diffCriteria).getLeftCriteria();
		doReturn(criteria).when(diffCriteria).getRightCriteria();
		doReturn(objectList1).doReturn(objectList2).when(handler).execute(criteria);
		List<Object> actualObjects = handler.executeDiff(diffCriteria);
		assertEquals(object1, actualObjects.get(0));
	}

	/**
	 * Execute or.
	 */
	@Test
	public void executeOr() {
		Object object1 = new Object();
		Object object2 = new Object();
		Object object3 = new Object();
		List<Object> objectList1 = new ArrayList<Object>();
		List<Object> objectList2 = new ArrayList<Object>();
		objectList1.add(object1);
		objectList1.add(object2);
		objectList2.add(object2);
		objectList2.add(object3);
		doReturn(new Criteria[] { criteria, criteria }).when(orCriteria).getCriterias();
		doReturn(objectList1).doReturn(objectList2).when(handler).execute(criteria);
		List<Object> actualObjects = handler.executeOr(orCriteria);
		assertTrue(actualObjects.contains(object1));
		assertTrue(actualObjects.contains(object2));
		assertTrue(actualObjects.contains(object3));
	}
	
	/**
	 * Gets the indexed key.
	 *
	 * @return the indexed key
	 */
	@Test
	public void getIndexedKey() {
		Runnable runnable = new Runnable() {
			@SuppressWarnings("unused")
			String runnable = "runnable";
			public void run() {}
		};
		assertEquals("runnable", handler.getIndexedKey("runnable",runnable));
	}
}
