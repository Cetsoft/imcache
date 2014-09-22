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

import com.cetsoft.imcache.cache.redis.client.exception.RedisConnectionException;
import com.cetsoft.imcache.cache.redis.client.util.RedisInputStream;
import com.cetsoft.imcache.cache.redis.client.util.RedisOutputStream;
import com.cetsoft.imcache.cache.redis.client.util.SafeEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private static final String ASK_RESPONSE = "ASK";
    private static final String MOVED_RESPONSE = "MOVED";
    private static final String CLUSTERDOWN_RESPONSE = "CLUSTERDOWN";

    public static final byte DOLLAR_BYTE = '$';
    public static final byte ASTERISK_BYTE = '*';
    public static final byte PLUS_BYTE = '+';
    public static final byte MINUS_BYTE = '-';
    public static final byte COLON_BYTE = ':';

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

    public static Object read(final RedisInputStream is) {
        return process(is);
    }

    private static Object process(final RedisInputStream is) {
        try {
            int b = is.readByte();
            if (b == MINUS_BYTE) {
                processError(is);
            } else if (b == ASTERISK_BYTE) {
                int num = Integer.parseInt(is.readLine());
                if (num == -1) {
                    return null;
                }
                List<Object> ret = new ArrayList<Object>(num);
                for (int i = 0; i < num; i++) {
                    //TODO:UPDATE THIS
                    ret.add(process(is));
                }
            } else if (b == COLON_BYTE) {
                return processInteger(is);
            } else if (b == DOLLAR_BYTE) {
                return processBulkReply(is);
            } else if (b == PLUS_BYTE) {
                return processStatusCodeReply(is);
            } else {
                throw new RedisConnectionException("unknown reply: " + (char) b);
            }
        } catch (IOException e) {
            throw new RedisConnectionException(e);
        }

        return null;
    }

    private static void processError(final RedisInputStream is) {
        String message = is.readLine();
        if (message.startsWith(MOVED_RESPONSE)) {
        } else if (message.startsWith(ASK_RESPONSE)) {
        } else if (message.startsWith(CLUSTERDOWN_RESPONSE)) {
        }
    }

    private static Long processInteger(final RedisInputStream is) {
        String num = is.readLine();
        return Long.valueOf(num);
    }

    private static byte[] processBulkReply(final RedisInputStream is) {
        int len = Integer.valueOf(is.readLine());
        if (len == -1) {
            return null;
        }
        byte[] read = new byte[len];
        int offset = 0;
        try {
            while (offset < len) {
                int size = is.read(read, offset, (len - offset));
                if (size == -1) {
                    throw new RedisConnectionException("it seems that server has closed the connection");
                }

                offset += size;
            }

            // read 2 more bytes
            is.readByte();
            is.readByte();
        } catch (IOException e) {
            throw new RedisConnectionException(e);
        }

        return read;
    }

    private static byte[] processStatusCodeReply(final RedisInputStream is) {
        return SafeEncoder.encode(is.readLine());
    }
}
