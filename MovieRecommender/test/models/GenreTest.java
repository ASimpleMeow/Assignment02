package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GenreTest {

	Genre testGenre;
	
	@Before
	public void setup()
	{
		testGenre = new Genre(0,"Test");
	}
	
	@After
	public void tearDown()
	{
		testGenre = null;
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailIndex() {
		new Genre(-1,"Test");
		fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailGenre1() {
		new Genre(0,"");
		fail();
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorFailGenre2() {
		new Genre(1,null);
		fail();
	}
	
	@Test
	public void testConstructor()
	{
		Genre genre1 = new Genre(0,"test");
		Genre genre2 = new Genre(0,"test");
		Genre genre3 = genre2;
		
		assertEquals(genre1,genre2);
		assertSame(genre3,genre2);
	}
	
	@Test
	public void testGetters()
	{
		assertEquals(0,testGenre.getGenreIndex());
		assertEquals("Test",testGenre.getGenre());
	}

}
