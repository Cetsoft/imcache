package com.cetsoft.imcache.cache.redis.client.util;

import com.cetsoft.imcache.cache.redis.client.exception.RedisCacheException;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RedisInputStream extends FilterInputStream {

    protected final byte buf[];

    protected int count, limit;

    public RedisInputStream(InputStream in, int size) {
        super(in);
        if (size <= 0) {
            throw new IllegalArgumentException("Buffer size <= 0");
        }
        buf = new byte[size];
    }

    public RedisInputStream(InputStream in) {
        this(in, 8192);
    }

    public byte readByte() throws IOException {
        if (count == limit) {
            fill();
        }

        return buf[count++];
    }

    public String readLine() {
        int b;
        byte c;
        StringBuilder sb = new StringBuilder();

        try {
            while (true) {
                if (count == limit) {
                    fill();
                }
                if (limit == -1)
                    break;

                b = buf[count++];
                if (b == '\r') {
                    if (count == limit) {
                        fill();
                    }

                    if (limit == -1) {
                        sb.append((char) b);
                        break;
                    }

                    c = buf[count++];
                    if (c == '\n') {
                        break;
                    }
                    sb.append((char) b);
                    sb.append((char) c);
                } else {
                    sb.append((char) b);
                }
            }
        } catch (IOException e) {
            throw new RedisCacheException(e);
        }
        String reply = sb.toString();
        if (reply.length() == 0) {
            throw new RedisCacheException(
                    "It seems like server has closed the connection.");
        }
        return reply;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (count == limit) {
            fill();
            if (limit == -1)
                return -1;
        }
        final int length = Math.min(limit - count, len);
        System.arraycopy(buf, count, b, off, length);
        count += length;
        return length;
    }

    private void fill() throws IOException {
        limit = in.read(buf);
        count = 0;
    }
}