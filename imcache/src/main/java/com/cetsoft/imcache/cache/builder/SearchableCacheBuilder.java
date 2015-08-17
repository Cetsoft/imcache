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
* Date   : Aug 17, 2015
*/
package com.cetsoft.imcache.cache.builder;

import java.util.List;

import com.cetsoft.imcache.cache.search.ConcurrentIndexHandler;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.index.IndexType;

public abstract class SearchableCacheBuilder extends AbstractCacheBuilder{
	
	/** The is searchable. */
	protected volatile boolean isSearchable = false;
	
	/** The query executer. */
	protected IndexHandler<Object, Object> indexHandler;
	
	/** The Constant QUERY_EXECUTER. */
	protected static final IndexHandler<Object, Object> QUERY_EXECUTER = new IndexHandler<Object, Object>() {
		public void addIndex(String attributeName, IndexType type) {}

		public void remove(Object key, Object value) {}

		public List<Object> execute(Query query) {return null;}

		public void clear() {}

		public void add(Object key, Object value) {}
	};

	/**
	 * Instantiates a new searchable cache builder.
	 */
	protected SearchableCacheBuilder() {
		super();
		indexHandler = QUERY_EXECUTER;
	}
	
	/**
	 * Searchable.
	 */
	protected void searchable() {
		if (!isSearchable) {
			indexHandler = new ConcurrentIndexHandler<Object, Object>();
			isSearchable = true;
		}
	}
}
