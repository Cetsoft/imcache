package com.cetsoft.imcache.examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cetsoft.imcache.bytebuffer.OffHeapByteBufferStore;
import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.SimpleItem;
import com.cetsoft.imcache.cache.VersionedItem;
import com.cetsoft.imcache.cache.builder.CacheBuilder;
import com.cetsoft.imcache.cache.offheap.StaleItemException;

public class VersionedOffHeapCacheExample {
	
	public static void main(String [] args){
		OffHeapByteBufferStore bufferStore = new OffHeapByteBufferStore(10000,10); 
		final Cache<String, VersionedItem<String>> cache = CacheBuilder.versionedOffHeapCache().
		storage(bufferStore).build();
		ExecutorService service = Executors.newFixedThreadPool(3);
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
							String newValue = getRandomString();
							if(value==null){
								cache.put("apple", new SimpleItem<String>(newValue));
							}
							else{
								value.update(newValue);
								cache.put("apple", value);
							}
							System.out.println(value.getValue());
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
	
	private static String getRandomString(){
		char[] chars = new char[5];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = (char) (Math.random()*25+65);
		}
		return new String(chars);
	}
}
