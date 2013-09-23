imcache
=======

Imcache is a Java Caching Library. It supports various kinds of caching models that have been applied so far. 
Imcache intends to speed up applications by providing a means to manage cached data. It offers solutions ranging 
from small applications to large scale applications.
###The Cache Interface
Cache interfaces provides general methods that is implemented by all imcache caches. See the methods below.
```java
  public interface Cache<K, V> {
	  void put(K key, V value);
	  V get(K key);
	  V invalidate(K key);
	  void clear();
	  double hitRatio();
  }
```
###The Cache Builder
Cache Builder is one of the core asset of the imcache. You can create simple heapCaches to complexOffHeapCaches via 
Cache Builder. Let's see Cache Builder in action below.
```java
  void example(){
  	Cache<Integer,Integer> cache = CacheBuilder.heapCache().cacheLoader(new CacheLoader<Integer, Integer>() {
		public Integer load(Integer key) {
			return null;
		}
	}).capacity(10000).build(); 
  }
```
###The Cache Loader
The CacheLoader interface for loading values with specified keys. The class that is interested in loading values 
from a resource implements this interface. When data is not found the cache, load method of CacheLoader is called.
###The Eviction Listener
The listener interface for receiving eviction events. The class that is interested in processing a eviction event
implements this interface. When the eviction event occurs,that object's onEviction method is invoked.
###The Heap Cache
HeapCache uses LRU(Least Recently Used) as eviction strategy by the help of LinkedHashMap. As a result, 
HeapCache discards the least recently used items first when eviction required. Eviction occurs if the size of
the cache is equal to the cache capacity in a put operation.
###The Concurrent Heap Cache
ConcurrentHeapCache uses LRU(Least Recently Used) as eviction strategy by the help of ConcurrentLinkedHashMap. 
As a result, ConcurrentHeapCache discards the least recently used items firstwhen eviction required.
Eviction occurs if the size of the cache is equal to the cache capacity in a put operation
###The Off Heap Cache
The Class OffHeapCache is a cache that uses offheap byte buffers to store or retrieve data by serializing
items into bytes. To do so, OffHeapCache uses pointers to point array location of an item. OffHeapCache clears
the buffers periodically to gain free space if buffers are dirty(unused memory). It also does eviction depending on
access time to the objects.
To make offheap cache avaialable to be used JVM Parameters "-XX:MaxDirectMemorySize=4g" must be set.

