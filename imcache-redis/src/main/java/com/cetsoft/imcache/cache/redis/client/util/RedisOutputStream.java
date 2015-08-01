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
* Date   : Sep 8, 2014
*/
package com.cetsoft.imcache.cache.redis.client.util;


import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The Class RedisOutputStream.
 */
public final class RedisOutputStream extends FilterOutputStream {
    
    /** The buffer. */
    protected final byte buffer[];

    /** The count. */
    protected int count;

    /**
     * Instantiates a new redis output stream.
     *
     * @param out the out
     */
    public RedisOutputStream(final OutputStream out) {
        this(out, 8192);
    }

    /**
     * Instantiates a new redis output stream.
     *
     * @param out the out
     * @param size the size
     */
    public RedisOutputStream(final OutputStream out, final int size) {
        super(out);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        buffer = new byte[size];
    }

    /**
     * Flush buffer.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void flushBuffer() throws IOException {
        if (count > 0) {
            out.write(buffer, 0, count);
            count = 0;
        }
    }

    /**
     * Writes the byte to the buffer.
     *
     * @param b the b
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void write(final byte b) throws IOException {
        if (count == buffer.length) {
            flushBuffer();
        }
        buffer[count++] = b;
    }

    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[])
     */
    public void write(final byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#write(byte[], int, int)
     */
    public void write(final byte b[], final int off, final int len)
            throws IOException {
        if (len >= buffer.length) {
            flushBuffer();
            out.write(b, off, len);
        } else {
            if (len >= buffer.length - count) {
                flushBuffer();
            }

            System.arraycopy(b, off, buffer, count, len);
            count += len;
        }
    }

    /**
     * Write ascii cr lf.
     *
     * @param in the in
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeAsciiCrLf(final String in) throws IOException {
        final int size = in.length();

        for (int i = 0; i != size; ++i) {
            if (count == buffer.length) {
                flushBuffer();
            }
            buffer[count++] = (byte) in.charAt(i);
        }
        writeCrLf();
    }

    /**
     * Checks if is surrogate.
     *
     * @param ch the ch
     * @return true, if is surrogate
     */
    public static boolean isSurrogate(final char ch) {
        return ch >= Character.MIN_SURROGATE && ch <= Character.MAX_SURROGATE;
    }

    /**
     * Utf8 length.
     *
     * @param str the str
     * @return the int
     */
    public static int utf8Length(final String str) {
        int strLen = str.length(), utfLen = 0;
        for (int i = 0; i != strLen; ++i) {
            char c = str.charAt(i);
            if (c < 0x80) {
                utfLen++;
            } else if (c < 0x800) {
                utfLen += 2;
            } else if (isSurrogate(c)) {
                i++;
                utfLen += 4;
            } else {
                utfLen += 3;
            }
        }
        return utfLen;
    }

    /**
     * Write cr lf.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeCrLf() throws IOException {
        if (2 >= buffer.length - count) {
            flushBuffer();
        }
        buffer[count++] = '\r';
        buffer[count++] = '\n';
    }

    /**
     * Write utf8 cr lf.
     *
     * @param str the str
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeUtf8CrLf(final String str) throws IOException {
        int strLen = str.length();

        int i;
        for (i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            if (!(c < 0x80))
                break;
            if (count == buffer.length) {
                flushBuffer();
            }
            buffer[count++] = (byte) c;
        }

        for (; i < strLen; i++) {
            char c = str.charAt(i);
            if (c < 0x80) {
                if (count == buffer.length) {
                    flushBuffer();
                }
                buffer[count++] = (byte) c;
            } else if (c < 0x800) {
                if (2 >= buffer.length - count) {
                    flushBuffer();
                }
                buffer[count++] = (byte) (0xc0 | (c >> 6));
                buffer[count++] = (byte) (0x80 | (c & 0x3f));
            } else if (isSurrogate(c)) {
                if (4 >= buffer.length - count) {
                    flushBuffer();
                }
                int uc = Character.toCodePoint(c, str.charAt(i++));
                buffer[count++] = ((byte) (0xf0 | ((uc >> 18))));
                buffer[count++] = ((byte) (0x80 | ((uc >> 12) & 0x3f)));
                buffer[count++] = ((byte) (0x80 | ((uc >> 6) & 0x3f)));
                buffer[count++] = ((byte) (0x80 | (uc & 0x3f)));
            } else {
                if (3 >= buffer.length - count) {
                    flushBuffer();
                }
                buffer[count++] = ((byte) (0xe0 | ((c >> 12))));
                buffer[count++] = ((byte) (0x80 | ((c >> 6) & 0x3f)));
                buffer[count++] = ((byte) (0x80 | (c & 0x3f)));
            }
        }

        writeCrLf();
    }

    /** The Constant sizeTable. */
    private final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999,
            9999999, 99999999, 999999999, Integer.MAX_VALUE };

    /** The Constant DigitTens. */
    private final static byte[] DigitTens = { '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1',
            '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3',
            '3', '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4',
            '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
            '5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7',
            '7', '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8',
            '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9',
            '9', '9', '9', };

    /** The Constant DigitOnes. */
    private final static byte[] DigitOnes = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', };

    /** The Constant digits. */
    private final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z' };

    /**
     * Write int cr lf.
     *
     * @param value the value
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeIntCrLf(int value) throws IOException {
        if (value < 0) {
            write((byte) '-');
            value = -value;
        }

        int size = 0;
        while (value > sizeTable[size])
            size++;

        size++;
        if (size >= buffer.length - count) {
            flushBuffer();
        }

        int q, r;
        int charPos = count + size;

        while (value >= 65536) {
            q = value / 100;
            r = value - ((q << 6) + (q << 5) + (q << 2));
            value = q;
            buffer[--charPos] = DigitOnes[r];
            buffer[--charPos] = DigitTens[r];
        }

        for (;;) {
            q = (value * 52429) >>> (16 + 3);
            r = value - ((q << 3) + (q << 1));
            buffer[--charPos] = digits[r];
            value = q;
            if (value == 0)
                break;
        }
        count += size;

        writeCrLf();
    }

    /* (non-Javadoc)
     * @see java.io.FilterOutputStream#flush()
     */
    public void flush() throws IOException {
        flushBuffer();
        out.flush();
    }
}