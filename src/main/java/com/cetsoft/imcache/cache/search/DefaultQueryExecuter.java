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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cetsoft.imcache.cache.search.criteria.AndCriteria;
import com.cetsoft.imcache.cache.search.criteria.ArithmeticCriteria;
import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.criteria.DiffCriteria;
import com.cetsoft.imcache.cache.search.criteria.OrCriteria;
import com.cetsoft.imcache.cache.search.index.CacheIndex;
import com.cetsoft.imcache.cache.search.index.IndexNotFoundException;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.cache.search.index.NonUniqueHashIndex;
import com.cetsoft.imcache.cache.search.index.RangeIndex;
import com.cetsoft.imcache.cache.search.index.UniqueHashIndex;

/**
 * The Class DefaultQueryExecuter implements basic query execution.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class DefaultQueryExecuter<K,V> implements QueryExecuter<K, V>{
	
	/** The indexes. */
	protected Map<String, CacheIndex> indexes;
	
	/**
	 * Instantiates a new simple query executer.
	 */
	public DefaultQueryExecuter() {
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
	public List<K> execute(Query query) {
		List<K> results = execute(query.getCriteria());
		return results;
	}
	
	@SuppressWarnings("unchecked")
	protected List<K> execute(Criteria criteria){
		if(criteria instanceof ArithmeticCriteria){
			ArithmeticCriteria arithmeticCriteria = ((ArithmeticCriteria)criteria);
			CacheIndex cacheIndex = indexes.get(arithmeticCriteria.getAttributeName());
			if(cacheIndex==null){
				throw new IndexNotFoundException();
			}
			return (List<K>) arithmeticCriteria.meets(cacheIndex);
		}
		else{
			if(criteria instanceof AndCriteria){
				AndCriteria andCriteria = (AndCriteria)criteria;
				List<K> results = new ArrayList<K>();
				for (Criteria innerCriteria : andCriteria.getCriterias()) {
					List<K> result = execute(innerCriteria);
					if(results.size()==0){
						results.addAll(result);
					}
					else{
						List<K> mergedResults = new ArrayList<K>(results.size());
						for (K k : result) {
							if(results.contains(k)){
								mergedResults.add(k);
							}
						}
						results = mergedResults;
					}
				}
				return results;
			}
			else if(criteria instanceof OrCriteria){
				Set<K> results = new HashSet<K>();
				OrCriteria orCriteria = (OrCriteria)criteria;
				for (Criteria innerCriteria : orCriteria.getCriterias()) {
					List<K> result = execute(innerCriteria);
					results.addAll(result);
				}
				return new ArrayList<K>(results);
			}
			else{
				DiffCriteria diffCriteria = (DiffCriteria)criteria;
				List<K> leftResult = execute(diffCriteria.getLeftCriteria());
				List<K> rightResult = execute(diffCriteria.getRightCriteria());
				for (K k : rightResult) {
					leftResult.remove(k);
				}
				return leftResult;
			}
		}
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
