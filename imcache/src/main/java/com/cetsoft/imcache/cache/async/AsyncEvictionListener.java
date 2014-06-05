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

import com.cetsoft.imcache.cache.EvictionListener;

/**
 * The listener interface for receiving asyncEviction events.
 * The class that is interested in processing a asyncEviction
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAsyncEvictionListener<code> method. When
 * the asyncEviction event occurs, that object's appropriate
 * method is invoked.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public interface AsyncEvictionListener<K, V> extends EvictionListener<K, V>{
}
