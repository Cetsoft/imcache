package com.cetsoft.imcache.cache;

/**
 * The enum Cache category.
 */
public enum CacheCategory {
  /**
   * Heap cache category.
   */
  HEAP,
  /**
   * Offheap cache category.
   */
  OFFHEAP,
  /**
   * Versioned offheap cache category.
   */
  VERSIONED_OFFHEAP,
  /**
   * Redis cache category.
   */
  REDIS
}
