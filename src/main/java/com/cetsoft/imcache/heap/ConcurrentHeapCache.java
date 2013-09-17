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
* Date   : Sep 17, 2013
*/
package com.cetsoft.imcache.heap;

import java.util.concurrent.atomic.AtomicLong;

import com.cetsoft.imcache.AbstractCache;

public class ConcurrentHeapCache<K, V> extends AbstractCache<K, V> {

	/** The hit. */
	protected AtomicLong hit = new AtomicLong();

	/** The miss. */
	protected AtomicLong miss = new AtomicLong();

	public void put(K key, V value) {

	}

	public V get(K key) {
		return null;
	}

	public V invalidate(K key) {
		return null;
	}

	public boolean contains(K key) {
		return false;
	}

	public void clear() {

	}

	public double hitRatio() {
		return hit.get() / (hit.get() + miss.get());
	}
}