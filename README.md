imcache
=======
Imcache is a Java Caching Library.Imcache intends to speed up applications by providing a means to manage cached data. It offers solutions ranging from small applications to large scale applications. It supports n-level caching hierarchy where it supports various kind of caching methods like heap, offheap, and more. Imcache also supports well-known caching solution, redis. To extend, you can define a heapcache backed by offheap cache which is also backed by database. If a key is not found in heap, it will be asked to offheap and so on. 

[![Build Status](https://travis-ci.org/Cetsoft/imcache.svg)](https://travis-ci.org/Cetsoft/imcache)
[![Coverage Status](https://coveralls.io/repos/Cetsoft/imcache/badge.svg?branch=master&service=github)](https://coveralls.io/github/Cetsoft/imcache?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.cetsoft/imcache/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.cetsoft/imcache/)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html) [![Join the chat at https://gitter.im/Cetsoft/imcache](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Cetsoft/imcache?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

### Reference
In order to use imcache, you need to specify your dependency as follows:
#### Maven
```xml
<dependency>
  <groupId>com.cetsoft</groupId>
  <artifactId>imcache</artifactId>
  <version>0.3.0</version><!--Can be updated for later versions-->
</dependency>
```
#### Gradle
```groovy
compile group: 'com.cetsoft', name: 'imcache', version: '0.3.0'
```
For full dependency reference: https://mvnrepository.com/artifact/com.cetsoft/imcache
### Simple Application
```java
final Cache<String,User> cache = CacheBuilder.heapCache().
cacheLoader(new CacheLoader<String, User>() {
    public User load(String key) {
        return userDAO.get(key);
    }
}).evictionListener(new EvictionListener<String, User>{
    public onEviction(String key, User user){
        userDAO.save(key, user);
    }
}).capacity(10000).build();
//If there is not a user in the heap cache it'll be loaded via userDAO.
final User user = cache.get("#unique identifier"); 
final User newUser = new User("email", "Richard", "Murray")
//When maximum value for cache size is reached, eviction event occurs.
//In case of eviction, newUser will be saved to db.
cache.put(newUser.getEmail(), newUser);
```
### The Cache Interface
Imcache supports simple operation defined by the cache interface. Cache interface provides general methods that is implemented by all imcache caches. See the methods below.
```java
public interface Cache<K, V> {
    void put(K key, V value);
    void put(K key, V value, TimeUnit expiryUnit, long expiry);
    V get(K key);
    V invalidate(K key);
    void clear();
    CacheStats stats();
    String getName();
    int size();
}
```
### The Cache Builder
Cache Builder is one of the core asset of the imcache. You can create simple heapCaches to complexOffHeapCaches via 
Cache Builder. Let's see Cache Builder in action below.
```java
void example(){
    final Cache<Integer,Integer> cache = CacheBuilder.heapCache().
    cacheLoader(new CacheLoader<Integer, Integer>() {
  	//Here you can load the key from another cache like offheapcache
        public Integer load(Integer key) {
            return null;
        }
    }).capacity(10000).build(); 
}
```
### The Cache Loader
The CacheLoader interface for loading values with specified keys. The class that is interested in loading values 
from a resource implements this interface. When data is not found the cache, load method of CacheLoader is called.
### The Eviction Listener
The listener interface for receiving eviction events. The class that is interested in processing a eviction event
implements this interface. When the eviction event occurs,that object's onEviction method is invoked.
### The Heap Cache
HeapCache uses LRU(Least Recently Used) as eviction strategy by the help of caffeine. 
As a result, caffeine discards the least recently used items first when eviction required.
Eviction occurs if the size of the cache is equal to the cache capacity in a put operation
### The Off Heap Cache
The Class OffHeapCache is a cache that uses offheap byte buffers to store or retrieve data by serializing
items into bytes. To do so, OffHeapCache uses pointers to point array location of an item. OffHeapCache clears
the buffers periodically to gain free space if buffers are dirty(unused memory). It also does eviction depending on
access time to the objects.
To make offheap cache work to JVM Parameters <b>"-XX:MaxDirectMemorySize=4g"</b> must be set. Buffer capacity of 8 mb 
is a good choice to start OffHeapCache. Let's see sample OffHeapCache use.
```java
void example(){
    //8388608 is 8 MB and 10 buffers. 8MB*10 = 80 MB.
    final OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(8388608, 10);
    final Cache<Integer,SimpleObject> offHeapCache = CacheBuilder.offHeapCache().
    storage(bufferStore).build();
}
```
By default configuration, OffHeapCache will try to clean the places which are not used and marked as 
dirty periodically. What is more, it will do eviction periodically, too.

### The Versioned Off Heap Cache
The Class VersionedOffHeapCache is a type of offheap cache where cache items have versions that are incremented for each update.
To make versioned off heap cache work to JVM Parameters <b>"-XX:MaxDirectMemorySize=4g"</b> must be set. Buffer capacity of 8 mb 
is a good choice to start VersionedOffHeapCache. Let's see sample VersionedOffHeapCache use.
```java
void example(){
    //8388608 is 8 MB and 10 buffers. 8MB*10 = 80 MB.
    final OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(8388608, 10);
    final Cache<Integer,VersionedItem<SimpleObject>> offHeapCache = 
    CacheBuilder.versionedOffHeapCache().storage(bufferStore).build();
    VersionedItem<SimpleObject> versionedItem = offHeapCache.get(12);
}
```

### Redis Cache
RedisCache is a cache that uses redis server for storing or retrieving data by serializing items into bytes. Please check out [redis documentation](http://redis.io/documentation), and [download redis server](http://redis.io/download). Redis Cache doesn't have any capability of managing or dealing with redis cluster. Redis Cache can only talk to one redis server for the time being.
```java
void example(){
    Cache<Integer, String> cache = CacheBuilder.redisCache().
			hostName(HOSTNAME).port(PORT).build();
    cache.put(1, "apple");
    System.out.println(cache.get(1));
}
```

### Searching, Indexing and Query Execution
imcache provides searching for all the caches by default. Searching is done by execute method of SearchableCache.
Execute method takes a Query as an input and returns results as list. A query consists of criteria and filter. Here
is an example use for queries.
```java
void example(){
    SearchableCache<Integer, SimpleObject> cache = CacheBuilder.heapCache().
    addIndex("j", IndexType.RANGE_INDEX).build();
    cache.put(0, createObject());
    cache.put(1, createObject());
    cache.put(2, createObject());
    List<SimpleObject> objects = cache.execute(CacheQuery.newQuery().
    setCriteria(new BetweenCriteria("j",1,3).or(new ETCriteria("j", 3))).
    setFilter(new LEFilter("k", 3)));
    for (SimpleObject simpleObject : objects) {
        System.out.println(simpleObject);
    }
}
```
Note that queries to caches that live outside of JVM can't be executed. Thus, querying redis and memcache isn't possible.

<i>To learn more about imcache please look at examples provided.</i>

## Support
### Contributing
#### Compiling Project
`./mvnw install -DskipTests -Dgpg.skip`
#### License Header
`./mvnw license:format`
#### Coverage
`./mvnw clean verify -Dgpg.skip`
Open `${module}/target/site/jacoco/index.html` where module is imcache-core, imcache-heap and etc.
