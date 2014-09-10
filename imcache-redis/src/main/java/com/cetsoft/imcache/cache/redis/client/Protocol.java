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
 * Date   : Sep 8, 2014
 */
package com.cetsoft.imcache.cache.redis.client;

import com.cetsoft.imcache.cache.redis.client.exception.RedisConnectionException;
import com.cetsoft.imcache.cache.redis.client.util.RedisOutputStream;
import com.cetsoft.imcache.cache.redis.client.util.SafeEncoder;

import java.io.IOException;

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

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';

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
			raw = SafeEncoder.encode(this.name());
		}
	}

    public static void sendCommand(final RedisOutputStream os, final Command command, final byte[]... args) {
        sendCommand(os, command.raw, args);
    }

    public static void sendCommand(final RedisOutputStream os, final byte[] command, final byte[]... args) {
        try {
            os.write(ASTERISK_BYTE);
            os.writeIntCrLf(args.length);
            os.write(DOLLAR_BYTE);
            os.writeIntCrLf(command.length);
            os.write(command);
            os.writeCrLf();

            for (final byte[] arg : args) {
                os.write(DOLLAR_BYTE);
                os.writeIntCrLf(arg.length);
                os.write(arg);
                os.writeCrLf();
            }
        } catch (IOException e) {
            throw new RedisConnectionException(e);
        }
    }

}
