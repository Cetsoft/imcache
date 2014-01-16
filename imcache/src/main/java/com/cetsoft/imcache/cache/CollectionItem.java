package com.cetsoft.imcache.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cetsoft.imcache.cache.search.filter.Filter;

public class CollectionItem<V> implements CacheItem<Collection<V>>{

	private List<? extends V> objects;
	
	public CollectionItem(Collection<? extends V> objects){
		this.objects = new ArrayList<V>(objects);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<V> getValue() {
		return (Collection<V>) objects;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<V> filter(Filter filter){
		List<Object> filteredObjects = filter.filter((List<Object>) objects);
		return (Collection<V>) filteredObjects;
	}

}
