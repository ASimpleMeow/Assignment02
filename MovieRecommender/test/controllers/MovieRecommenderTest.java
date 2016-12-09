package controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Item;
import models.User;
import utils.Serializer;
import utils.XMLSerializer;

public class MovieRecommenderTest {

	RecommenderAPI movieRec;
	
	@Before
	public void setup()
	{
		movieRec =  new MovieRecommender(null,false);
	}
	
	@After
	public void tearDown()
	{
		movieRec = null;
	}
	
	@Test
	public void testGetUsers() {
		assertEquals(943,movieRec.getUsers().size());
	}

	@Test
	public void testGetMovies() {
		assertEquals(1679,movieRec.getMovies().size());
	}
	
	@Test
	public void testGetUser() {
		User testUser = new User(15,"Amy","Davis",49,'F',"educator");
		assertEquals(testUser,movieRec.getUser("Amy","Davis"));
		assertEquals(testUser,movieRec.getUser(15));
		
	}

	@Test
	public void testGetMovie() {
		ArrayList<String> genres = new ArrayList<String>();
		genres.add("Comedy");
		genres.add("Romance");
		Item testItem = new Item(49,"I.Q. (1994)","01-Jan-1994","http://us.imdb.com/M/title-exact?I.Q.%20(1994)",genres);
		assertEquals(testItem,movieRec.getMovie("I.Q. (1994)","01-Jan-1994"));
		assertEquals(testItem,movieRec.getMovie(49));
	}
	
	@Test
	public void testExists()
	{
		User testUser = movieRec.getUser(123);
		Item testItem = movieRec.getMovie(123);
		
		assertEquals(true,movieRec.userExist(123));
		assertEquals(true,movieRec.getUsers().containsValue(testUser));
		
		assertEquals(true,movieRec.itemExist(123));
		assertEquals(true,movieRec.getMovies().containsValue(testItem));
	}
	
	@Test
	public void testAddUser()
	{
		assertEquals(943,movieRec.getUsers().size());
		User testUser = new User(944,"TestUser", "TestUserLast", 19, 'M', "None");
		movieRec.addUser("TestUser", "TestUserLast", 19, 'M', "None");
		assertEquals(testUser, movieRec.getUser(944));
		assertEquals(944,movieRec.getUsers().size());
		
		movieRec.addUser("TestUser2", "TestUserLast2", 19, 'M', "None");
		assertEquals(945,movieRec.getUsers().size());
	}
	
	@Test
	public void testRemoveUser()
	{
		assertEquals(943,movieRec.getUsers().size());
		movieRec.addUser("TestUser", "TestUserLast", 19, 'M', "None");
		assertEquals(944,movieRec.getUsers().size());
		movieRec.removeUser(944);
		assertEquals(943,movieRec.getUsers().size());
	}
	
	@Test
	public void testAddMovie()
	{
		assertEquals(1679,movieRec.getMovies().size());
		Item testItem1 = new Item(1678,"Test","2016","www.test.com",new ArrayList<String>());
		ArrayList<String> genres = new ArrayList<String>();
		genres.add("comedy");
		Item testItem2 = new Item(1678,"Test2","2016","www.test.com",genres);
		movieRec.addMovie("Test", "2016", "www.test.com");
		movieRec.addMovie("Test2", "2016", "www.test.com",genres);
		assertEquals(1681,movieRec.getMovies().size());
		
		assertEquals(testItem1,movieRec.getMovie(1680));
		assertEquals(testItem2,movieRec.getMovie(1681));
	}
	
	@Test
	public void testRating()
	{
		movieRec.addUser("TestUser", "TestUserLast", 19, 'M', "None");
		movieRec.addRating(944, 1, 3);
		assertEquals(1,movieRec.getUser(944).getRatings().values().size());
		assertEquals(3,movieRec.getUser(944).getRatings().get(movieRec.getMovie(1)).getRating());
		
		movieRec.addRating(944, 1, 5);
		assertEquals(5,movieRec.getUser(944).getRatings().get(movieRec.getMovie(1)).getRating());
		
		movieRec.addRating(944, 1, -5);
		assertEquals(-5,movieRec.getUser(944).getRatings().get(movieRec.getMovie(1)).getRating());
		
		try
		{
			movieRec.addRating(944, 1, 6);
			fail();
		}
		catch(IllegalArgumentException e)
		{
			
		}
		try
		{
			movieRec.addRating(944, 1, -6);
			fail();
		}
		catch(IllegalArgumentException e)
		{
			
		}
	}
	
	@Test
	public void testGetUserRecommendations()
	{
		
	}
	
	@Test
	public void testUserTopTenMovies()
	{
		
	}
	
	@Test
	public void testGetTopTenMovies()
	{
		
	}
	
	@Test
	public void testWriteRead() throws Exception
	{
		File testFile = new File("test.xml");
		Serializer s = new XMLSerializer(testFile);
		RecommenderAPI mr = new MovieRecommender(s,testFile.exists());
		mr.addMovie("Test1", "2016", "www.none");
		mr.addUser("FirstNameTest", "LastNameTest", 19, 'M', "None");
		mr.addRating(944, 1680, 5);
		
		mr.write();
		RecommenderAPI mr2 = new MovieRecommender(s,testFile.exists());
		mr2.load();
		
		assertEquals(944, mr2.getUsers().size());
		assertEquals(1680,mr2.getMovies().size());
		assertEquals("FirstNameTest", mr2.getUser(944).getFirstName());
		assertEquals("Test1",mr2.getMovie(1680).getItemTitle());
		assertEquals(1, mr2.getUser(944).getRatings().size());
		assertEquals(5, mr2.getUser(944).getRatings().get(mr2.getMovie(1680)).getRating());
		
		if(testFile.exists())
			testFile.delete();
	}

}
