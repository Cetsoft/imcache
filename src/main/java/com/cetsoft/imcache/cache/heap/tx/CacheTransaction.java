package com.cetsoft.imcache.cache.heap.tx;

import java.util.Stack;

public class CacheTransaction implements Transaction{
	
	private int transactionId;
	private static int transactionCounter;
	private static ThreadLocal<CacheTransaction> cacheTransaction = new ThreadLocal<CacheTransaction>();
	private static ThreadLocal<Stack<TransactionLog>> transactionThreadLocal = new ThreadLocal<Stack<TransactionLog>>();
	
	public CacheTransaction(int transactionId) {
		this.transactionId = transactionId;
	}
	
	public static Transaction get(){
		if(cacheTransaction.get()==null){
			cacheTransaction.set(new CacheTransaction(transactionCounter++));
		}
		return cacheTransaction.get();
	}
	
	public static void addLog(TransactionLog log){
		if(transactionThreadLocal.get()!=null){
			transactionThreadLocal.get().push(log);
		}
		else{
			throw new TransactionException("There is no transaction available!");
		}
	}
	
	public void begin() {
		if(transactionThreadLocal.get()==null){
			transactionThreadLocal.set(new Stack<TransactionLog>());
		}
	}

	public void commit() {
		Stack<TransactionLog> logs = transactionThreadLocal.get();
		while (!logs.isEmpty()) {
			logs.pop().apply();
		}
		cacheTransaction.set(null);
		transactionThreadLocal.set(null);
	}

	public void rollback() {
		Stack<TransactionLog> logs = transactionThreadLocal.get();
		while (!logs.isEmpty()) {
			logs.pop().rollback();
		}
	}

	public int getStatus() {
		return 0;
	}

	public int getTransactionId() {
		return transactionId;
	}

}
