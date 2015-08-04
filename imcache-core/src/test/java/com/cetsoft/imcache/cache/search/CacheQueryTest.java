package com.cetsoft.imcache.cache.search;

import org.junit.Test;
import static org.junit.Assert.*;

import com.cetsoft.imcache.cache.search.criteria.Criteria;
import com.cetsoft.imcache.cache.search.criteria.ETCriteria;
import com.cetsoft.imcache.cache.search.filter.Filter;
import com.cetsoft.imcache.cache.search.filter.LTFilter;

public class CacheQueryTest {
	
	@Test
	public void cacheQuery(){
		Criteria etCriteria = new ETCriteria("id", 3);
		Filter ltFilter = new LTFilter("age", 18);
		Query query = CacheQuery.newQuery().setCriteria(etCriteria).setFilter(ltFilter);
		assertEquals(etCriteria, query.getCriteria());
		assertEquals(ltFilter, query.getFilter());
	}
}
