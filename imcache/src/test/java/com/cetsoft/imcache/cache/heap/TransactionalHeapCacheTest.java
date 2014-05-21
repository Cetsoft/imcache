package com.cetsoft.imcache.cache.heap;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.CacheLoader;
import com.cetsoft.imcache.cache.EvictionListener;
import com.cetsoft.imcache.cache.heap.tx.CacheTransaction;
import com.cetsoft.imcache.cache.heap.tx.TransactionCommitter;
import com.cetsoft.imcache.cache.heap.tx.TransactionException;
import com.cetsoft.imcache.cache.search.IndexHandler;

public class TransactionalHeapCacheTest {

	@Mock
	CacheLoader<Object, Object> cacheLoader;

	@Mock
	EvictionListener<Object, Object> evictionListener;

	@Mock
	IndexHandler<Object, Object> indexHandler;

	@Mock
	TransactionCommitter<Object, Object> committer;

	TransactionalHeapCache<Object, Object> cache;

	@Mock
	Map<Object, Object> map;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		cache = spy(new TransactionalHeapCache<Object, Object>(committer, cacheLoader, evictionListener, indexHandler,
				1000));
		cache.cache = map;
	}

	@Test
	public void putSuccess() {
		doReturn("1").when(cache.cache).get(1);
		doNothing().when(committer).doPut(any(), any());
		CacheTransaction.get().begin();
		try {
			cache.put(1, "2");
			CacheTransaction.get().commit();
		} catch (TransactionException exception) {
			CacheTransaction.get().rollback();
		} finally {
			CacheTransaction.get().close();
		}
		verify(committer).doPut(any(), any());
	}

	@Test
	public void putFail() {
		doReturn("1").when(cache.cache).get(1);
		doThrow(new TransactionException(new Exception())).when(committer).doPut(any(), any());
		CacheTransaction.get().begin();
		try {
			cache.put(1, "2");
			CacheTransaction.get().commit();
		} catch (TransactionException exception) {
			CacheTransaction.get().rollback();
		} finally {
			CacheTransaction.get().close();
		}
		verify(cache.cache, times(2)).put(any(), any());
	}
}
