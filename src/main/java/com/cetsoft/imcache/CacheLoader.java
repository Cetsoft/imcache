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
package com.cetsoft.imcache;

/**
 * The CacheLoader interface for loading values with specified keys. 
 * The class that is interested in loading values from a resource 
 * implements this interface, and the object created with that 
 * class is registered with a component using the component's 
 * <code>addCacheLoader<code> method. When the loading is requested, 
 * that object's appropriate method is invoked.
 * 
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public interface CacheLoader<K, V> {

	/**
	 * Loads the value with specified key.
	 * 
	 * @param key
	 *            the key
	 * @return the value
	 */
	V load(K key);
}
