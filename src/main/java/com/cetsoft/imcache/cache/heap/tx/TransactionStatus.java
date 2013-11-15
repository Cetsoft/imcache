package com.cetsoft.imcache.cache.heap.tx;

public enum TransactionStatus {
    ACTIVE,
    BEGAN,
    BEGINNING,
    COMMITTING,
    COMMITTED,
    COMMIT_FAILED,
    ROLLING_BACK,
    ROLLED_BACK,
    COMPLETED
}
