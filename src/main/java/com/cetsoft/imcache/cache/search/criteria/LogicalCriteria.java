package com.cetsoft.imcache.cache.search.criteria;


public abstract class LogicalCriteria implements Criteria{

	public Criteria and(Criteria criteria) {
		return new AndCriteria(this, criteria);
	}

	public Criteria or(Criteria criteria) {
		return new OrCriteria(this, criteria);
	}

	public Criteria diff(Criteria criteria) {
		return new DiffCriteria(this, criteria);
	}

}
