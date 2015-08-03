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
* Date   : Sep 2, 2014
*/
/***********************************************************************
 * Copyright 2010 Google Inc. All Rights Reserved.
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
 ************************************************************************/
package com.cetsoft.imcache.concurrent;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Linked list implementation of the Deque interface (not available in Java 5, so not
 * formally declared) where the link pointers are tightly integrated with the element.
 * Linked deques have no capacity restrictions; they grow as necessary to support usage.
 * They are not thread-safe; in the absence of external synchronization, they do not
 * support concurrent access by multiple threads. Null elements are prohibited.
 * <p>
 * Most <tt>LinkedDeque</tt> operations run in constant time by assuming that the
 * {@link Linked} parameter is associated with the deque instance. Any usage that violates
 * this assumption will result in non-deterministic behavior.
 * <p>
 * The iterators returned by this class are <em>not</em> <i>fail-fast</i>: If the deque is
 * modified at any time after the iterator is created, the iterator will be in an unknown
 * state. Thus, in the face of concurrent modification, the iterator risks arbitrary,
 * non-deterministic behavior at an undetermined time in the future.
 * 
 * @param <E> the type of elements held in this collection
 */
public class LinkedDeque<E extends Linked<E>> extends AbstractCollection<E> {

	// This class provides a doubly-linked list that is optimized for the
	// virtual
	// machine. The first and last elements are manipulated instead of a
	// slightly
	// more convenient sentinel element to avoid the insertion of null checks
	// with
	// NullPointerException throws in the byte code. The links to a removed
	// element are cleared to help a generational garbage collector if the
	// discarded elements inhabit more than one generation.

	/**
	 * Pointer to first node. Invariant: (first == null && last == null) || (first.prev ==
	 * null)
	 */
	E first;

	/**
	 * Pointer to last node. Invariant: (first == null && last == null) || (last.next ==
	 * null)
	 */
	E last;

	/**
	 * Links the element to the front of the deque so that it becomes the first element.
	 * 
	 * @param e the unlinked element
	 */
	void linkFirst(final E e) {
		final E f = first;
		first = e;

		if (f == null) {
			last = e;
		} else {
			f.setPrevious(e);
			e.setNext(f);
		}
	}

	/**
	 * Links the element to the back of the deque so that it becomes the last element.
	 * 
	 * @param e the unlinked element
	 */
	void linkLast(final E e) {
		final E l = last;
		last = e;

		if (l == null) {
			first = e;
		} else {
			l.setNext(e);
			e.setPrevious(l);
		}
	}

	/**
	 *  Unlinks the non-null first element.
	 *
	 * @return the e
	 */
	E unlinkFirst() {
		final E f = first;
		final E next = f.getNext();
		f.setNext(null);

		first = next;
		if (next == null) {
			last = null;
		} else {
			next.setPrevious(null);
		}
		return f;
	}

	/**
	 *  Unlinks the non-null last element.
	 *
	 * @return the e
	 */
	E unlinkLast() {
		final E l = last;
		final E prev = l.getPrevious();
		l.setPrevious(null);
		last = prev;
		if (prev == null) {
			first = null;
		} else {
			prev.setNext(null);
		}
		return l;
	}

	/**
	 *  Unlinks the non-null element.
	 *
	 * @param e the e
	 */
	void unlink(E e) {
		final E prev = e.getPrevious();
		final E next = e.getNext();

		if (prev == null) {
			first = next;
		} else {
			prev.setNext(next);
			e.setPrevious(null);
		}

		if (next == null) {
			last = prev;
		} else {
			next.setPrevious(prev);
			e.setNext(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return (first == null);
	}

	/**
	 * Check not empty.
	 */
	void checkNotEmpty() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Beware that, unlike in most collections, this method is <em>NOT</em> a
	 * constant-time operation.
	 */
	@Override
	public int size() {
		int size = 0;
		for (E e = first; e != null; e = e.getNext()) {
			size++;
		}
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#clear()
	 */
	@Override
	public void clear() {
		for (E e = first; e != null;) {
			E next = e.getNext();
			e.setPrevious(null);
			e.setNext(null);
			e = next;
		}
		first = last = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return (o instanceof Linked<?>) && contains((Linked<?>) o);
	}

	// A fast-path containment check
	/**
	 * Contains.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	boolean contains(Linked<?> e) {
		return (e.getPrevious() != null) || (e.getNext() != null) || (e == first);
	}

	/**
	 * Moves the element to the front of the deque so that it becomes the first element.
	 * 
	 * @param e the linked element
	 */
	public void moveToFront(E e) {
		if (e != first) {
			unlink(e);
			linkFirst(e);
		}
	}

	/**
	 * Moves the element to the back of the deque so that it becomes the last element.
	 * 
	 * @param e the linked element
	 */
	public void moveToBack(E e) {
		if (e != last) {
			unlink(e);
			linkLast(e);
		}
	}

	/**
	 * Peek.
	 *
	 * @return the e
	 */
	public E peek() {
		return peekFirst();
	}

	/**
	 * Peek first.
	 *
	 * @return the e
	 */
	public E peekFirst() {
		return first;
	}

	/**
	 * Peek last.
	 *
	 * @return the e
	 */
	public E peekLast() {
		return last;
	}

	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public E getFirst() {
		checkNotEmpty();
		return peekFirst();
	}

	/**
	 * Gets the last.
	 *
	 * @return the last
	 */
	public E getLast() {
		checkNotEmpty();
		return peekLast();
	}

	/**
	 * Element.
	 *
	 * @return the e
	 */
	public E element() {
		return getFirst();
	}

	/**
	 * Offer.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public boolean offer(E e) {
		return offerLast(e);
	}

	/**
	 * Offer first.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public boolean offerFirst(E e) {
		if (contains(e)) {
			return false;
		}
		linkFirst(e);
		return true;
	}

	/**
	 * Offer last.
	 *
	 * @param e the e
	 * @return true, if successful
	 */
	public boolean offerLast(E e) {
		if (contains(e)) {
			return false;
		}
		linkLast(e);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		return offerLast(e);
	}

	/**
	 * Adds the first.
	 *
	 * @param e the e
	 */
	public void addFirst(E e) {
		if (!offerFirst(e)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Adds the last.
	 *
	 * @param e the e
	 */
	public void addLast(E e) {
		if (!offerLast(e)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Poll.
	 *
	 * @return the e
	 */
	public E poll() {
		return pollFirst();
	}

	/**
	 * Poll first.
	 *
	 * @return the e
	 */
	public E pollFirst() {
		if (isEmpty()) {
			return null;
		}
		return unlinkFirst();
	}

	/**
	 * Poll last.
	 *
	 * @return the e
	 */
	public E pollLast() {
		if (isEmpty()) {
			return null;
		}
		return unlinkLast();
	}

	/**
	 * Removes the.
	 *
	 * @return the e
	 */
	public E remove() {
		return removeFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean remove(Object o) {
		if (contains(o)) {
			unlink((E) o);
			return true;
		}
		return false;
	}

	/**
	 * Removes the first.
	 *
	 * @return the e
	 */
	public E removeFirst() {
		checkNotEmpty();
		return pollFirst();
	}

	/**
	 * Removes the first occurrence.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public boolean removeFirstOccurrence(Object o) {
		return remove(o);
	}

	/**
	 * Removes the last.
	 *
	 * @return the e
	 */
	public E removeLast() {
		checkNotEmpty();
		return pollLast();
	}

	/**
	 * Removes the last occurrence.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	public boolean removeLastOccurrence(Object o) {
		return remove(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean modified = false;
		for (Object o : c) {
			modified |= remove(o);
		}
		return modified;
	}

	/**
	 * Push.
	 *
	 * @param e the e
	 */
	public void push(E e) {
		addFirst(e);
	}

	/**
	 * Pop.
	 *
	 * @return the e
	 */
	public E pop() {
		return removeFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		return new AbstractLinkedIterator(first) {

			@Override
			E computeNext() {
				return cursor.getNext();
			}
		};
	}

	/**
	 * Descending iterator.
	 *
	 * @return the iterator
	 */
	public Iterator<E> descendingIterator() {
		return new AbstractLinkedIterator(last) {

			@Override
			E computeNext() {
				return cursor.getPrevious();
			}
		};
	}

	/**
	 * The Class AbstractLinkedIterator.
	 */
	abstract class AbstractLinkedIterator implements Iterator<E> {

		/** The cursor. */
		E cursor;

		/**
		 * Creates an iterator that can can traverse the deque.
		 * 
		 * @param start the initial element to begin traversal from
		 */
		AbstractLinkedIterator(E start) {
			cursor = start;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return (cursor != null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#next()
		 */
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			E e = cursor;
			cursor = computeNext();
			return e;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}

		/**
		 * Retrieves the next element to traverse to or <tt>null</tt> if there are no more
		 * elements.
		 *
		 * @return the e
		 */
		abstract E computeNext();
	}
}
