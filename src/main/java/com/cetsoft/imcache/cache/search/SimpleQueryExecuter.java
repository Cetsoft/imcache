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
* Date   : Sep 28, 2013
*/
package com.cetsoft.imcache.cache.search;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class SimpleQueryExecuter implements basic query execution.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class SimpleQueryExecuter<K,V> implements QueryExecuter<K, V>{
	
	/** The indexes. */
	protected Map<Attribute, Map<Object, K>> indexes;
	
	/**
	 * Instantiates a new simple query executer.
	 */
	public SimpleQueryExecuter() {
		indexes = new HashMap<Attribute, Map<Object,K>>();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.Indexable#addIndex(com.cetsoft.imcache.cache.search.CacheIndex)
	 */
	public void addIndex(CacheIndex index) {
		indexes.put(index.getIndex(), new HashMap<Object, K>());
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.QueryExecuter#add(java.lang.Object, java.lang.Object)
	 */
	public void add(K key, V value) {
		for(Attribute attribute:indexes.keySet()){
			Object indexedKey = getIndexedKey(attribute,value);
			if(indexedKey==null){
				throw new NullPointerException();
			}
			indexes.get(attribute).put(indexedKey, key);
		}
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.QueryExecuter#remove(java.lang.Object, java.lang.Object)
	 */
	public void remove(K key, V value) {
		for(Attribute attribute:indexes.keySet()){
			Object indexedKey = getIndexedKey(attribute,value);
			if(indexedKey==null){
				throw new NullPointerException();
			}
			indexes.get(attribute).remove(indexedKey);
		}
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.QueryExecuter#clear()
	 */
	public void clear() {
		indexes.clear();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.QueryExecuter#execute(com.cetsoft.imcache.cache.search.Query)
	 */
	public List<K> execute(Query query) {
		
		return null;
	}
	
	/**
	 * Gets the indexed key.
	 *
	 * @param attribute the attribute
	 * @param value the value
	 * @return the indexed key
	 */
	private Object getIndexedKey(Attribute attribute, V value) {
		try {
			Field field = value.getClass().getDeclaredField(attribute.getName());
			field.setAccessible(true);
			return field.get(value);
		} catch (Exception e) {
			throw new AttributeException(e);
		}
	}
	
	/**
	 * The Class AttributeException.
	 */
	private static class AttributeException extends RuntimeException{

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 8883617514611224481L;

		/**
		 * Instantiates a new attribute exception.
		 *
		 * @param exception the exception
		 */
		public AttributeException(Exception exception){
			super(exception);
		}
	}

}
