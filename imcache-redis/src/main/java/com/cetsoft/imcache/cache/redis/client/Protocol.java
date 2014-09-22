/*
* Copyright (C) 2014 Cetsoft, http://www.cetsoft.com
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
* Date   : Sep 8, 2014
*/
package com.cetsoft.imcache.cache.redis.client;

/**
 * The Class Protocol.
 */
public final class Protocol {

	/** The Constant DEFAULT_PORT. */
	public static final int DEFAULT_PORT = 6379;

	/** The Constant DEFAULT_SENTINEL_PORT. */
	public static final int DEFAULT_SENTINEL_PORT = 26379;

	/** The Constant DEFAULT_TIMEOUT. */
	public static final int DEFAULT_TIMEOUT = 2000;

	/** The Constant DEFAULT_DATABASE. */
	public static final int DEFAULT_DATABASE = 0;

	/** The Constant CHARSET. */
	public static final String CHARSET = "UTF-8";

	/**
	 * The Enum Command.
	 */
	public static enum Command {

		/** The ping. */
		PING,
		/** The set. */
		SET,
		/** The get. */
		GET,
		/** The quit. */
		QUIT,
		/** The exists. */
		EXISTS,
		/** The del. */
		DEL,
		/** The type. */
		TYPE,
		/** The flushdb. */
		FLUSHDB,
		/** The keys. */
		KEYS,
		/** The expire. */
		EXPIRE,
		/** The expireat. */
		EXPIREAT,
		/** The ttl. */
		TTL;

		/** The raw bytes. */
		public final byte[] raw;

		/**
		 * Instantiates a new command.
		 */
		Command() {
			raw = null;
		}
	}
}
