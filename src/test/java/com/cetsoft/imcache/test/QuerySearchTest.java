package com.cetsoft.imcache.test;

import java.util.List;

import com.cetsoft.imcache.cache.SearchableCache;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.search.CacheQuery;
import com.cetsoft.imcache.cache.search.criteria.BetweenCriteria;
import com.cetsoft.imcache.cache.search.index.IndexType;

public class QuerySearchTest {

	public static void main(String []args){
//		SearchableCache<Integer, SimpleObject> cache = CacheBuilder.heapCache().addIndex("j", IndexType.UNIQUE_HASH).build();
//		cache.put(0, createObject(0, 1));
//		cache.put(1, createObject(1, 2));
//		List<SimpleObject> objects = cache.execute(CacheQuery.newQuery().addCriteria(new EqualsToCriteria("j",2)));
//		for (SimpleObject simpleObject : objects) {
//			System.out.println(simpleObject);
//		}
		SearchableCache<Integer, SimpleObject> cache = CacheBuilder.heapCache().addIndex("j", IndexType.RANGE_INDEX).build();
		cache.put(0, createObject(1, 1));
		cache.put(1, createObject(2, 2));
		cache.put(2, createObject(3, 3));
		List<SimpleObject> objects = cache.execute(CacheQuery.newQuery().addCriteria(new BetweenCriteria("j",1,3)));
		for (SimpleObject simpleObject : objects) {
			System.out.println(simpleObject);
		}
	}
	
	private static SimpleObject createObject(int i,int j){
		SimpleObject object = new SimpleObject();
		object.setI(i);
		object.setJ(j);
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
