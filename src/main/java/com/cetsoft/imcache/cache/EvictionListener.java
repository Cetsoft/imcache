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
* Date   : Sep 15, 2013
*/
package com.cetsoft.imcache.cache;

/**
 * The listener interface for receiving eviction events.
 * The class that is interested in processing a eviction
 * event implements this interface,When the eviction event occurs, 
 * that object's onEviction method is invoked.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @see EvictionEvent
 */
public interface EvictionListener<K, V> {
	
	/**
	 * Called on eviction.
	 *
	 * @param key the key
	 * @param value the value
	 */
	void onEviction(K key, V value);
}
