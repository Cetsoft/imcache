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

