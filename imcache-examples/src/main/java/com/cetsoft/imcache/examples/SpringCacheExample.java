package com.cetsoft.imcache.examples;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringCacheExample {

	public static void main(String [] args){
		ApplicationContext context = new ClassPathXmlApplicationContext("exampleContext.xml");
	}
}
