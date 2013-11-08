package com.cetsoft.imcache.cache.search.criteria;


public class OrCriteria extends LogicalCriteria {
	
	private Criteria[] criterias;
	
	public OrCriteria(Criteria ... criterias){
		this.criterias = criterias;
	}

	public Criteria[] getCriterias() {
		return criterias;
	}
}
