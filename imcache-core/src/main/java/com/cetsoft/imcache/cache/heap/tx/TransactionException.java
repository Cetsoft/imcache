/*
* Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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

/**
 * The Class TransactionException.
 */
public class TransactionException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1513928931641021361L;

	/**
	 * Instantiates a new transaction exception.
	 *
	 * @param exception the exception
	 */
	public TransactionException(String exception){
		super(exception);
	}
	
	/**
	 * Instantiates a new transaction exception.
	 *
	 * @param exception the exception
	 */
	public TransactionException(Exception exception){
		super(exception);
	}
}
