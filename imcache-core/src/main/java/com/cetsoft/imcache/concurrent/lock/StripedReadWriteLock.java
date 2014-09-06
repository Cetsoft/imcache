package com.cetsoft.imcache.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The StripedReadWriteLock is lock holder that contains array of
 * {@link java.util.concurrent.locks.ReentrantReadWriteLock}, on which lock/unlock
 * operations are performed. Purpose of this is to decrease lock contention.
 * When a lock requested, this lock gives a lock associated with the given id. 
 */
public class StripedReadWriteLock {
	
	private final ReentrantReadWriteLock[] locks;

	/**
	 * Default factor, creates 16 locks
	 */
	public StripedReadWriteLock() {
		this(4);
	}

	/**
	 * Creates array of locks, size of array may be any from set {2^1, 2^2, ..., 2^11}
	 * 
	 * @param storagePower size of array will be equal to 2^storagePower
	 */
	public StripedReadWriteLock(int storagePower) {
		if (!(storagePower >= 1 && storagePower <= 11)) {
			throw new IllegalArgumentException("storage power must be in {1..11}");
		}

		int lockSize = (int) Math.pow(2, storagePower);
		locks = new ReentrantReadWriteLock[lockSize];
		for (int i = 0; i < locks.length; i++){
			locks[i] = new ReentrantReadWriteLock();
		}
	}

	/**
	 * Locks lock associated with given id.
	 * 
	 * @param id value, from which lock is derived
	 */
	public void readLock(int id) {
		getLock(id).readLock().lock();
	}

	/**
	 * Unlocks lock associated with given id.
	 * 
	 * @param id value, from which lock is derived
	 */
	public void readUnlock(int id) {
		getLock(id).readLock().unlock();
	}

	/**
	 * Locks lock associated with given id.
	 * 
	 * @param id value, from which lock is derived
	 */
	public void writeLock(int id) {
		getLock(id).writeLock().lock();
	}

	/**
	 * Unlocks lock associated with given id.
	 * 
	 * @param id value, from which lock is derived
	 */
	public void writeUnlock(int id) {
		getLock(id).writeLock().unlock();
	}

	/**
	 * Finds the lock associated with the id
	 * 
	 * @param id value, from which lock is derived
	 * @return lock which is associated with the id
	 */
	private ReentrantReadWriteLock getLock(int id) {
		// locks.length-1 is a string of ones since lock.length is power of 2,
		// thus ending cancels out the higher bits of id and leaves the lower bits
		// to determine the lock.
		return locks[id & (locks.length - 1)];
	}
}