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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.index.CacheIndex;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.cache.search.index.NonUniqueHashIndex;
import com.cetsoft.imcache.cache.search.index.RangeIndex;
import com.cetsoft.imcache.cache.search.index.UniqueHashIndex;

/**
 * The Class SimpleQueryExecuter implements basic query execution.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class SimpleQueryExecuter<K,V> implements QueryExecuter<K, V>{
	
	/** The indexes. */
	protected Map<String, CacheIndex> indexes;
	
	/**
	 * Instantiates a new simple query executer.
	 */
	public SimpleQueryExecuter() {
		indexes = new HashMap<String, CacheIndex>();
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.Indexable#addIndex(com.cetsoft.imcache.cache.search.CacheIndex)
	 */
	public void addIndex(String attributeName, IndexType type) {
		if(type==IndexType.UNIQUE_HASH){
			indexes.put(attributeName, new UniqueHashIndex());
		}
		else if(type==IndexType.NON_UNIQUE_HASH){
			indexes.put(attributeName, new NonUniqueHashIndex());
		}
		else if(type==IndexType.RANGE_INDEX){
			indexes.put(attributeName, new RangeIndex());
		}
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.QueryExecuter#add(java.lang.Object, java.lang.Object)
	 */
	public void add(K key, V value) {
		for(String attributeName:indexes.keySet()){
			Object indexedKey = getIndexedKey(attributeName,value);
			if(indexedKey==null){
				throw new NullPointerException();
			}
			indexes.get(attributeName).put(indexedKey, key);
		}
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.search.QueryExecuter#remove(java.lang.Object, java.lang.Object)
	 */
	public void remove(K key, V value) {
		for(String attributeName:indexes.keySet()){
			Object indexedKey = getIndexedKey(attributeName,value);
			if(indexedKey==null){
				throw new NullPointerException();
			}
			indexes.get(attributeName).remove(indexedKey,key);
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
	@SuppressWarnings("unchecked")
	public List<K> execute(Query query) {
		List<K> result = new ArrayList<K>();
		for (Criteria criteria : query.criterias()) {
			result= (List<K>) criteria.meets(indexes.get(criteria.getAttributeName()));
		}
		return result;
	}
	
	/**
	 * Gets the indexed key.
	 *
	 * @param attribute the attribute
	 * @param value the value
	 * @return the indexed key
	 */
	private Object getIndexedKey(String attributeName, V value) {
		try {
			Field field = value.getClass().getDeclaredField(attributeName);
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
