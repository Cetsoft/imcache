package com.cetsoft.imcache.examples;

import java.util.List;

import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.search.CacheQuery;
import com.cetsoft.imcache.cache.search.criteria.BetweenCriteria;
import com.cetsoft.imcache.cache.search.criteria.ETCriteria;
import com.cetsoft.imcache.cache.search.filter.LEFilter;
import com.cetsoft.imcache.cache.search.index.IndexType;

public class QuerySearchExample {

	public static void main(String []args){
		SearchableCache<Integer, SimpleObject> cache = CacheBuilder.heapCache().addIndex("j", IndexType.RANGE_INDEX).build();
		cache.put(0, createObject(1, 1));
		cache.put(1, createObject(2, 2));
		cache.put(2, createObject(3, 3));
		List<SimpleObject> objects = cache.execute(CacheQuery.newQuery().setCriteria(new BetweenCriteria("j",1,3).
				or(new ETCriteria("j", 3))).setFilter(new LEFilter("k", 3)));
		for (SimpleObject simpleObject : objects) {
			System.out.println(simpleObject);
		}
	}
	
	private static SimpleObject createObject(int i,int j){
		SimpleObject object = new SimpleObject();
		object.setI(i);
		object.setJ(j);
		object.setK((int) (Math.random()*5));
		return object;
	}
	
	@SuppressWarnings("unused")
	private static class SimpleObject{
		private int i,j,k;
		private String name = "test";
		
		public int getI() {
			return i;
		}
		public void setI(int i) {
			this.i = i;
		}
		public int getJ() {
			return j;
		}
		public void setJ(int j) {
			this.j = j;
		}
		public int getK() {
			return k;
		}
		public void setK(int k) {
			this.k = k;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return "SimpleObject [i=" + i + ", j=" + j + ", k=" + k + ", name=" + name + "]";
		}
	}
}
