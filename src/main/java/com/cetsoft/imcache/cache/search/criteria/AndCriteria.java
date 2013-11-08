package com.cetsoft.imcache.cache.search.criteria;


public class AndCriteria extends LogicalCriteria {
	
	private Criteria[] criterias;
	
	public AndCriteria(Criteria ... criterias){
		this.criterias = criterias;
	}

	public Criteria[] getCriterias() {
		return criterias;
	}
}
