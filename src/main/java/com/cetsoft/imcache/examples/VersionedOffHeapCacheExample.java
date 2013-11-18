package com.cetsoft.imcache.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.SimpleItem;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.offheap.StaleItemException;
import com.cetsoft.imcache.serialization.Serializer;

public class VersionedOffHeapCacheExample {
	
	public static void main(String [] args){
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(10000,10); 
		final Cache<String, VersionedItem<String>> cache =CacheBuilder.versionedOffHeapCache().
		storage(bufferStore).serializer(new Serializer<String>(){
			public byte[] serialize(String value) {
				return com.cetsoft.imcache.examples.Serializer.serialize(value);
			}
			public String deserialize(byte[] payload) {
				return com.cetsoft.imcache.examples.Serializer.deserialize(payload);
			}
		}).build();
		ExecutorService service = Executors.newFixedThreadPool(200);
		for (int i = 0; i < 100000; i++) {
			service.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					while(true){
						try{
							VersionedItem<String> value = cache.get("apple");
							if(value==null){
								cache.put("apple", new SimpleItem<String>("apple"));
							}
							else{
								cache.put("apple", value);
							}
							break;
						}catch(StaleItemException ex){
							ex.printStackTrace();
						}
					}
					VersionedItem<String> item = cache.get("apple");
					System.out.println(item.getVersion());
				}
			});
		}
	}
}
