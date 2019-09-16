package com.cetsoft.imcache.cache;

public interface CacheStats {

  long getHitCount();

  long getMissCount();

  long getLoadCount();

  long getEvictionCount();

  long requestCount();

  double hitRate();

  double missRate();
}
