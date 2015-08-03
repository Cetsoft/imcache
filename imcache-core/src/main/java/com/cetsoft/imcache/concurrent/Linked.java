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
* Date   : Aug 3, 2015
*/
package com.cetsoft.imcache.concurrent;

/**
 * An element that is linked on the Deque.
 */
public interface Linked<T extends Linked<T>> {

	/**
	 * Retrieves the previous element or <tt>null</tt> if either the element is unlinked
	 * or the first element on the deque.
	 */
	T getPrevious();

	/** Sets the previous element or <tt>null</tt> if there is no link. */
	void setPrevious(T prev);

	/**
	 * Retrieves the next element or <tt>null</tt> if either the element is unlinked or
	 * the last element on the deque.
	 */
	T getNext();

	/** Sets the next element or <tt>null</tt> if there is no link. */
	void setNext(T next);
}
