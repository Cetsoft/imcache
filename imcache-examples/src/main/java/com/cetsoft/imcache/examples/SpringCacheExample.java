package com.cetsoft.imcache.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringCacheExample {

	@Autowired
	CacheManager cacheManager;
	
	public static void main(String [] args){
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("exampleContext.xml");
		SpringCacheExample example = context.getBean(SpringCacheExample.class);
		example.getBook(0);
		example.getBook(0);
	}
	
	@Cacheable("books")
	public Book getBook(int id){
		return new Book();
	}
	
	public class Book{
		private int id;
		private String name;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

}
