/*
* Copyright (C) 2013 Cetsoft, http://www.cetsoft.com
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Library General Public
* License as published by the Free Software Foundation; either
* version 2 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Library General Public License for more details.
*
* You should have received a copy of the GNU Library General Public
* License along with this library; if not, write to the Free
* Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
* 
* Author : Yusuf Aytas
* Date   : Sep 23, 2013
*/
package com.cetsoft.imcache.cache.heap.tx;

import java.util.Stack;

/**
 * The Class CacheTransaction.
 */
public class CacheTransaction implements Transaction{
	
	/** The transaction id. */
	private int transactionId;
	
	/** The transaction counter. */
	private static int transactionCounter;
	
	/** The transaction status. */
	private TransactionStatus transactionStatus;
	
	/** The cache transaction. */
	private static ThreadLocal<CacheTransaction> cacheTransaction = new ThreadLocal<CacheTransaction>();
	
	/** The transaction thread local. */
	private static ThreadLocal<Stack<TransactionLog>> transactionThreadLocal = new ThreadLocal<Stack<TransactionLog>>();
	
	/**
	 * Instantiates a new cache transaction.
	 *
	 * @param transactionId the transaction id
	 */
	public CacheTransaction(int transactionId) {
		this.transactionId = transactionId;
		this.transactionStatus = TransactionStatus.ACTIVE;
	}
	
	/**
	 * Gets the.
	 *
	 * @return the transaction
	 */
	public static Transaction get(){
		if(cacheTransaction.get()==null){
			cacheTransaction.set(new CacheTransaction(transactionCounter++));
		}
		return cacheTransaction.get();
	}
	
	/**
	 * Adds the log.
	 *
	 * @param log the log
	 */
	public static void addLog(TransactionLog log){
		if(get().getStatus()!=TransactionStatus.BEGAN){
			throw new TransactionException("Transaction either did not begin or aldready committed.");
		}
		if(transactionThreadLocal.get()!=null){
			transactionThreadLocal.get().push(log);
		}
		else{
			throw new TransactionException("There is no transaction available!");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.heap.tx.Transaction#begin()
	 */
	public void begin() {
		if(this.transactionStatus!=TransactionStatus.ACTIVE){
			throw new TransactionException("Transaction began already or is beginning.");
		}
		this.transactionStatus = TransactionStatus.BEGINNING;
		transactionThreadLocal.set(new Stack<TransactionLog>());
		this.transactionStatus = TransactionStatus.BEGAN;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.heap.tx.Transaction#commit()
	 */
	public void commit() {
		if(this.transactionStatus==TransactionStatus.ACTIVE){
			throw new TransactionException("Transaction did not begin.");
		}
		else if(this.transactionStatus!=TransactionStatus.BEGAN){
			throw new TransactionException("Transaction is either committing or committed already.");
		}
		this.transactionStatus = TransactionStatus.COMMITTING;
		Stack<TransactionLog> logs = transactionThreadLocal.get();
		for (TransactionLog log : logs) {
			try{
				log.apply();
			}catch(Exception exception){
				this.transactionStatus = TransactionStatus.COMMIT_FAILED;
				throw new TransactionException(exception);
			}
		}
		this.transactionStatus = TransactionStatus.COMMITTED;
		transactionThreadLocal.set(null);
		cacheTransaction.set(null);
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.heap.tx.Transaction#rollback()
	 */
	public void rollback() {
		if(this.transactionStatus!=TransactionStatus.COMMIT_FAILED){
			throw new TransactionException("Transaction does not need to rollback or already rolled back.");
		}
		this.transactionStatus = TransactionStatus.ROLLING_BACK;
		Stack<TransactionLog> logs = transactionThreadLocal.get();
		while (!logs.isEmpty()) {
			logs.pop().rollback();
		}
		this.transactionStatus = TransactionStatus.ROLLED_BACK;
	}

	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.heap.tx.Transaction#close()
	 */
	public void close() {
		this.transactionStatus = TransactionStatus.COMPLETED;
	}
	
	/* (non-Javadoc)
	 * @see com.cetsoft.imcache.cache.heap.tx.Transaction#getStatus()
	 */
	public TransactionStatus getStatus() {
		return transactionStatus;
	}

	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public int getTransactionId() {
		return transactionId;
	}


}
