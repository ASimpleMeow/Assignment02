package models;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class ItemTest {

	Item testItem;
	
	@Before
	public void setup()
	{
		testItem = new Item(1,"Movie","2016","www.none.com",new ArrayList<String>());
	}
	
	@After
	public void tearDown()
	{
		testItem = null;
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailID() {
		new Item(-1,"Movie","2016","www.none.com",new ArrayList<String>());
		fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailTitle() {
		new Item(1,"","2016","www.none.com",new ArrayList<String>());
		fail();
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorFailFirstName2() {
		new Item(1,null,"2016","www.none.com",new ArrayList<String>());
		fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailLastYear1() {
		new Item(1,"Movie","","www.none.com",new ArrayList<String>());
		fail();
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorFailLastYear2() {
		new Item(1,"Movie",null,"www.none.com",new ArrayList<String>());
		fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailUrl1() {
		new Item(1,"Movie","2016","",new ArrayList<String>());
		fail();
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorFailUrl2() {
		new Item(1,"Movie","2016",null,new ArrayList<String>());
		fail();
	}
	
	@Test
	public void testContructor()
	{
		List<String> genres1 = new ArrayList<String>();
		List<String> genres2 = new ArrayList<String>();
		List<String> genres3 = new ArrayList<String>();
		
		genres1.add("Horror");
		genres2.add("Comedy");
		genres3.addAll(genres1);
		genres3.addAll(genres2);
		
		
		Item item1 = new Item(1,"Movie","2016","www.none.com",new ArrayList<String>());
		Item item2 = new Item(1,"Movie2","2016","None",genres1);
		Item item3 = new Item(2,"Movie3","2016","None",genres2);
		Item item4 = new Item(1,"Movie2","2016","None",genres3);
		Item item5 = item1;
		
		assertEquals(0,item1.getGenres().size());
		assertEquals("Horror",item2.getGenres().get(0));
		assertEquals("Comedy",item3.getGenres().get(0));
		assertEquals(2,item4.getGenres().size());
		assertEquals(testItem,item1);
		assertSame(item5,item1);
	}
	
	@Test
	public void testGetters()
	{
		assertEquals(1,testItem.getItemID());
		assertEquals("Movie",testItem.getItemTitle());
		assertEquals("2016",testItem.getReleaseDate());
		assertEquals("www.none.com",testItem.getUrl());
		assertEquals(0,testItem.getGenres().size());
	}
	
	@Test
	public void testToString()
	{
		assertEquals("movieID: 1\nMovie Title: Movie\nRelease Date: 2016\nURL: www.none.com\nGenres: \n",testItem.toString());
	}

}
