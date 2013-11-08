package com.cetsoft.imcache.cache.search.criteria;


public class DiffCriteria extends LogicalCriteria {
	
	private Criteria leftCriteria, rightCriteria;
	
	public DiffCriteria(Criteria leftCriteria, Criteria rightCriteria){
		this.leftCriteria = leftCriteria;
		this.rightCriteria = rightCriteria;
	}

	public Criteria getLeftCriteria() {
		return leftCriteria;
	}

	public Criteria getRightCriteria() {
		return rightCriteria;
	}
	
}
