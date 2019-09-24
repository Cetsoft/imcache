/**
 * Copyright © 2013 Cetsoft. All rights reserved.
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
 */
package com.cetsoft.imcache.cache.concurrent;

import static org.junit.Assert.assertTrue;

import com.cetsoft.imcache.concurrent.StripedReadWriteLock;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class StripedReadWriteLockTest {

  static final int STOP = 2;
  AtomicInteger id = new AtomicInteger();
  StripedReadWriteLock lock = new StripedReadWriteLock();

  @Test
  public void readLockTest() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(1);
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        assertTrue(doubleReadLock(2));
        latch.countDown();
      }
    }).start();
    assertTrue(doubleReadLock(1));
    latch.await();
  }

  public boolean doubleReadLock(int lockId) {
    lock.readLock(lockId);
    try {
      id.set(lockId);
      while (lockId != STOP && lockId == id.get()) {
        ;
      }
      return true;
    } finally {
      lock.readUnlock(lockId);
    }
  }

  @Test
  public void readWriteTest() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(1);
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        assertTrue(doubleWriteLock(2));
        latch.countDown();
      }
    }).start();
    assertTrue(doubleWriteLock(1));
    latch.await();
  }

  public boolean doubleWriteLock(int lockId) {
    lock.writeLock(lockId);
    try {
      id.set(lockId);
      while (lockId != STOP && lockId == id.get()) {
        ;
      }
      return true;
    } finally {
      lock.writeUnlock(lockId);
    }
  }
}
