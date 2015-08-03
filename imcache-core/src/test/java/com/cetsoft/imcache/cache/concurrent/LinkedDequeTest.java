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
* Date   : Aug 4, 2015
*/
package com.cetsoft.imcache.cache.concurrent;

import java.util.ArrayList;

import org.junit.Test;

import com.cetsoft.imcache.concurrent.Linked;
import com.cetsoft.imcache.concurrent.LinkedDeque;

public class LinkedDequeTest {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void linkedDeque(){
		LinkedDeque deque = new LinkedDeque();
		deque.add(new Node<Integer>());
		deque.addAll(new ArrayList<Object>());
		deque.addFirst(new Node<Integer>());
		deque.addLast(new Node<Integer>());
		deque.clear();
		deque.contains(2);
		deque.descendingIterator();
		deque.iterator();
		deque.moveToBack(new Node<Integer>());
		deque.moveToFront(new Node<Integer>());
		deque.offer(new Node<Integer>());
		deque.offerFirst(new Node<Integer>());
		deque.offerLast(new Node<Integer>());
		deque.peek();
		deque.peekFirst();
		deque.peekLast();
		deque.poll();
		deque.pollFirst();
		deque.peekLast();
		deque.push(new Node<Integer>());
		deque.remove();
		deque.remove(2323);
		deque.removeFirst();
		deque.removeFirstOccurrence(232);
		deque.removeLast();
		deque.removeLastOccurrence(32);
		deque.size();
	}
	
	private static class Node<T> implements Linked<Node<T>>{
		
		Node<T> next, prev;

		@Override
		public Node<T> getPrevious() {
			return prev;
		}

		@Override
		public void setPrevious(Node<T> prev) {
			this.prev = prev;
		}

		@Override
		public Node<T> getNext() {
			return next;
		}

		@Override
		public void setNext(Node<T> next) {
			this.next = next;
		}
		
	}
}
