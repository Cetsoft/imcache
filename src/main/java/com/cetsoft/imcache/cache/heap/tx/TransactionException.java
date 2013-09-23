package com.cetsoft.imcache.cache.heap.tx;

public class TransactionException extends RuntimeException {

	private static final long serialVersionUID = -1513928931641021361L;

	public TransactionException(String exception){
		super(exception);
	}
	
	public TransactionException(Exception exception){
		super(exception);
	}
}
