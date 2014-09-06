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
* Date   : Jun 5, 2014
*/
package com.cetsoft.imcache.cache.async;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * The Class ScheduledEvictionListenerTest.
 */
public class ScheduledEvictionListenerTest {
	
	/** The cache task. */
	@Mock
	CacheTask<Object, Object> cacheTask;
	
	/** The cache tasks. */
	@Mock
	BlockingQueue<CacheTask<Object, Object>> cacheTasks;
	
	/** The async eviction listener. */
	ScheduledEvictionListener<Object, Object> asyncEvictionListener;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		asyncEvictionListener = spy( new ScheduledEvictionListener<Object, Object>() {
			@Override
			public void save(List<CacheTask<Object, Object>> cacheTasks) {}
		});
		asyncEvictionListener.cacheTasks = cacheTasks;
	}
	
	/**
	 * Inits the.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test
	public void init() throws InterruptedException{
		long period = 1;
		doNothing().when(asyncEvictionListener).drainQueue();
		asyncEvictionListener.init(period, 3);
		Thread.sleep(period*3);
		verify(asyncEvictionListener, atLeast(1)).drainQueue();
	}
	
	/**
	 * On eviction.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test
	public void onEviction() throws InterruptedException{
		Object key = new Object(), value = new Object();
		doNothing().when(cacheTasks).put(cacheTask);
		doReturn(cacheTask).when(asyncEvictionListener).createCacheTask(key, value);
		asyncEvictionListener.onEviction(key, value);
		verify(asyncEvictionListener).createCacheTask(key, value);
		verify(cacheTasks).put(cacheTask);
	}
	
	/**
	 * Creates the cache task.
	 */
	@Test
	public void createCacheTask(){
		Object key = new Object(), value = new Object();
		CacheTask<Object, Object> cacheTask = asyncEvictionListener.createCacheTask(key, value);
		assertEquals(key, cacheTask.getKey());
		assertEquals(value, cacheTask.getValue());
	}
	
	/**
	 * Drain queue.
	 */
	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void drainQueue(){
		final TasksHolder tasksHolder = new TasksHolder();
		doReturn(false).doReturn(true).when(cacheTasks).isEmpty();
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) throws Throwable {
				List<CacheTask<Object, Object>> tasks  = (List<CacheTask<Object, Object>>) invocation.getArguments()[0];
				tasks.add(cacheTask);
				tasksHolder.tasks = tasks;
				return null;
			}
		}).when(cacheTasks).drainTo(anyList(), anyInt());
		doNothing().when(asyncEvictionListener).save(anyList());
		asyncEvictionListener.drainQueue();
		assertEquals(cacheTask, tasksHolder.tasks.get(0));
	}
	
	/**
	 * The Class TasksHolder.
	 */
	private static class TasksHolder{
		
		/** The tasks. */
		List<CacheTask<Object, Object>> tasks;
	}

}
