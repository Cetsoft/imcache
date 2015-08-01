/*
* Copyright (C) 2015 Cetsoft, http://www.cetsoft.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* 
* Author : Yusuf Aytas
* Date   : Jan 6, 2014
*/
package com.cetsoft.imcache.cache.builder;

/**
 * The Class NecessaryArgumentException.
 */
public class NecessaryArgumentException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7420658148641484150L;

	/**
	 * Instantiates a new necessary argument exception.
	 *
	 * @param exception the exception
	 */
	public NecessaryArgumentException(String exception) {
		super(exception);
	}
}
