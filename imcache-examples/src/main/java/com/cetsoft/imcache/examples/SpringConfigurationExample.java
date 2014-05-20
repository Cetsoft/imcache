package com.cetsoft.imcache.examples;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cetsoft.imcache.cache.Cache;
import com.cetsoft.imcache.cache.CacheEntry;
import com.cetsoft.imcache.cache.pupulator.SimpleCachePupulator;
import com.cetsoft.imcache.cache.util.CacheUtils;

@Component
public class SpringConfigurationExample {
	
	@Autowired
	Cache<String,String> cache;
	
	final CacheDao cacheDao = new CacheDaoImpl(); 
	
	public static void main(String [] args){
		@SuppressWarnings({ "resource", "unused" })
		ApplicationContext context = new ClassPathXmlApplicationContext("exampleContext.xml");
	}
	
	@PostConstruct
	public void initCache(){
		new SimpleCachePupulator<String, String>(cache) {
			public List<CacheEntry<String, String>> loadEntries() {
				List<CacheEntry<String, String>>  cacheEntries = new ArrayList<CacheEntry<String,String>>();
				for (String cacheEntry : cacheDao.getAll()) {
					cacheEntries.add(CacheUtils.createEntry(cacheEntry, cacheEntry));
				}
				return cacheEntries;
			}
		}.pupulate();
		System.out.println(cache.get("orange"));
	}
	
	protected static interface CacheDao{
		List<String> getAll();
	}
	
	protected static class CacheDaoImpl implements CacheDao{
			
		public List<String> getAll() {
			List<String> fruits = new ArrayList<String>();
			fruits.add("orange");
			fruits.add("apple");
			fruits.add("kiwi");
			return fruits;
		}
	}
}
