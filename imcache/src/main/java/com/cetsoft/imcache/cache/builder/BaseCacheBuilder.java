/**
 * Copyright © 2013 Cetsoft. All rights reserved.
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
 */
package com.cetsoft.imcache.cache.builder;

import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotEmpty;
import static com.cetsoft.imcache.cache.util.ArgumentUtils.checkNotNull;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.ImcacheException;
import com.cetsoft.imcache.cache.search.DefaultIndexHandler;
import com.cetsoft.imcache.cache.search.IndexHandler;
import com.cetsoft.imcache.cache.search.Query;
import com.cetsoft.imcache.cache.search.index.IndexType;
import com.cetsoft.imcache.serialization.Serializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * The type Base cache builder.
 */
public abstract class BaseCacheBuilder {

  /**
   * The constant DEFAULT_CACHE_LOADER.
   */
  protected static final CacheLoader<Object, Object> DEFAULT_CACHE_LOADER = key -> null;

  /**
   * The constant DEFAULT_EVICTION_LISTENER.
   */
  protected static final EvictionListener<Object, Object> DEFAULT_EVICTION_LISTENER = (key, value) -> {
  };

  /**
   * The constant DEFAULT_SERIALIZER.
   */
  protected static final Serializer<Object> DEFAULT_SERIALIZER = new Serializer<Object>() {
    public byte[] serialize(Object object) {
      ByteArrayOutputStream bos = null;
      ObjectOutput out = null;
      try {
        bos = new ByteArrayOutputStream();
        out = new ObjectOutputStream(bos);
        out.writeObject(object);
        return bos.toByteArray();
      } catch (IOException e) {
        throw new ImcacheException(e);
      } finally {
        closeAll(bos, (Closeable) out);
      }
    }

    public Object deserialize(byte[] bytes) {
      ByteArrayInputStream bis = null;
      ObjectInput in = null;
      try {
        bis = new ByteArrayInputStream(bytes);
        in = new ObjectInputStream(bis);
        return in.readObject();
      } catch (IOException e) {
        throw new ImcacheException(e);
      } catch (ClassNotFoundException e) {
        throw new ImcacheException(e);
      } finally {
        closeAll(bis, (Closeable) in);
      }
    }
  };

  /**
   * The constant DEFAULT_QUERY_EXECUTOR.
   */
  protected static final IndexHandler<Object, Object> DEFAULT_QUERY_EXECUTOR = new IndexHandler<Object, Object>() {
    public void addIndex(final String attributeName, final IndexType type) {
    }

    public void remove(Object key, Object value) {
    }

    public List<Object> execute(Query query) {
      return null;
    }

    public void clear() {
    }

    public void add(Object key, Object value) {
    }
  };
  /**
   * The Cache loader.
   */
  protected CacheLoader<Object, Object> cacheLoader = DEFAULT_CACHE_LOADER;
  /**
   * The Eviction listener.
   */
  protected EvictionListener<Object, Object> evictionListener = DEFAULT_EVICTION_LISTENER;
  /**
   * The Serializer.
   */
  protected Serializer<Object> serializer = DEFAULT_SERIALIZER;
  /**
   * The Index handler.
   */
  protected IndexHandler<Object, Object> indexHandler = DEFAULT_QUERY_EXECUTOR;
  /**
   * The Is searchable.
   */
  protected volatile boolean isSearchable = false;
  /**
   * The Name.
   */
  protected String name;

  /**
   * Close all.
   *
   * @param closeables the closeables
   */
  public static void closeAll(final Closeable... closeables) {
    if (closeables == null || closeables.length == 0) {
      return;
    }
    try {
      for (final Closeable closeable : closeables) {
        if (closeable != null) {
          closeable.close();
        }
      }
    } catch (IOException e) {
      throw new ImcacheException(e);
    }
  }

  /**
   * Handle index.
   *
   * @param attributeName the attribute name
   * @param indexType the index type
   */
  protected synchronized void handleIndex(final String attributeName, final IndexType indexType) {
    checkNotEmpty(attributeName, "attribute name can't be empty");
    checkNotNull(indexType, "index type can't be null");
    if (!isSearchable) {
      isSearchable = true;
      indexHandler = new DefaultIndexHandler<>();
    }
    indexHandler.addIndex(attributeName, indexType);
  }

  /**
   * Build cache.
   *
   * @param <K> the type parameter
   * @param <V> the type parameter
   * @return the cache
   */
  public abstract <K, V> Cache<K, V> build();

  /**
   * Build cache.
   *
   * @param <K> the type parameter
   * @param <V> the type parameter
   * @param name the name
   * @return the cache
   */
  public abstract <K, V> Cache<K, V> build(final String name);

}
