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
		Thread.sleep(period*50);
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
