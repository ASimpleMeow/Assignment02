package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RatingTest {
	
	Rating rating;
	
	@Before
	public void setup()
	{
		rating = new Rating(1,2,3);
	}
	
	@After
	public void tearDown()
	{
		rating = null;
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testContructorUserIDFail()
	{
		new Rating(-1, 1,1);
		fail();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testContructorItemIDFail()
	{
		new Rating(1, -1,1);
		fail();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testContructorRatingHighFail()
	{
		new Rating(1, 1,6);
		fail();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testContructorRatingLowFail()
	{
		new Rating(1, 1,-6);
		fail();
	}
	
	@SuppressWarnings("unused")
	@Test 
	public void testContructor()
	{
		Rating rating1 = new Rating(5, 10,5);
		Rating rating2 = new Rating(0, 0,-5);
		Rating rating3 = new Rating(100, 100,0);
		Rating rating4 = new Rating(1,1,1);
		Rating rating5 = new Rating(5, 10,5);
		Rating rating6 = rating2;
		
		assertEquals(rating5,rating1);
		assertSame(rating6,rating2);
	}
	
	@Test
	public void testGetters()
	{
		
		assertEquals(1,rating.getUserID(),0.01);
		assertEquals(2,rating.getItemID(),0.01);
		assertEquals(3,rating.getRating(),0.01);
	}
	
	@Test
	public void testToString()
	{
		Rating rating = new Rating(1,2,3);
		assertEquals("Rating [userID=1, itemID=2, rating=3]",rating.toString());
	}
	
}
