package com.cetsoft.imcache.cache;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cetsoft.imcache.cache.search.filter.Filter;

public class CollectionItemTest {
	
	@Mock
	Filter filter;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void filter(){
		Item item = new Item(2);
		List<Item> items = new ArrayList<Item>();
		items.add(item);
		CollectionItem<Item> collectionItem = new CollectionItem<Item>(items);
		doReturn(items).when(filter).filter(anyList());
		Collection<Item> filteredItems = collectionItem.filter(filter);
		assertTrue(filteredItems.contains(item));
	}
	
	@SuppressWarnings("unused")
	private static class Item{
		int value;

		public Item(int value) {
			this.value = value;
		}
	}
}
