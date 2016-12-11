package controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Item;
import models.User;
import utils.Serializer;
import utils.XMLSerializer;

/**
 * JUNIT test for MovieRecommender class
 * @author Oleksandr Kononov
 *
 */
public class MovieRecommenderTest {

	RecommenderAPI movieRec;
	
	/**
	 * Runs before every test
	 */
	@Before
	public void setup()
	{
		movieRec =  new MovieRecommender(null,false);
	}
	
	/**
	 * Runs after every test
	 */
	@After
	public void tearDown()
	{
		movieRec = null;
	}
	
	//Testing getters//
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
	
	/**
	 * Testing for user and item existence
	 */
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
	
	/**
	 * Testing adding a user
	 * @throws Exception
	 */
	@Test
	public void testAddUser() throws Exception
	{
		assertEquals(943,movieRec.getUsers().size());
		User testUser = new User(944,"TestUser", "TestUserLast", 19, 'M', "None");
		movieRec.addUser("TestUser", "TestUserLast", 19, 'M', "None");
		assertEquals(testUser, movieRec.getUser(944));
		assertEquals(944,movieRec.getUsers().size());
		
		movieRec.addUser("TestUser2", "TestUserLast2", 19, 'M', "None");
		assertEquals(945,movieRec.getUsers().size());
	}
	
	/**
	 * Testing removing a user
	 * @throws Exception
	 */
	@Test
	public void testRemoveUser() throws Exception
	{
		assertEquals(943,movieRec.getUsers().size());
		movieRec.addUser("TestUser", "TestUserLast", 19, 'M', "None");
		assertEquals(944,movieRec.getUsers().size());
		movieRec.removeUser(944);
		assertEquals(943,movieRec.getUsers().size());
	}
	
	/**
	 * Testing adding a movie
	 * @throws Exception
	 */
	@Test
	public void testAddMovie() throws Exception
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
	
	/**
	 * Testing adding ratings 
	 * @throws Exception
	 */
	@Test
	public void testRating() throws Exception
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
	
	/**
	 * Testing getting user recommendations
	 * @throws Exception
	 */
	@Test
	public void testGetUserRecommendations() throws Exception
	{
		Random rn = new Random();
		movieRec.addUser("Test", "Test", 19, 'M', "None");
		List<Item> itemsRated = new ArrayList<Item>();
		for(int i=1;i<rn.nextInt(movieRec.getMovies().size()/2)+1;i++)
		{
			movieRec.addRating(944, i, -5+(rn.nextInt(10)));
			itemsRated.add(movieRec.getMovie(i));
		}
		for(Item item : movieRec.getUserRecommendations(944))
		{
			if(itemsRated.contains(item))
				fail("Movie has already been rated by User");
		}
	}
	
	/**
	 * Testing getting user specific top ten movies
	 * @throws Exception
	 */
	@Test
	public void testUserTopTenMovies() throws Exception
	{
		movieRec.addUser("Test", "Test", 19, 'M', "None");
		for(int i=1; i<movieRec.getMovies().size();i++)
		{
			if(i==5||i==15||i==25||i==35||i==45||i==55||i==65||i==85||i==95||i==100)
				movieRec.addRating(944, i, 5);
			else
				movieRec.addRating(944, i, new Random().nextInt(3));
		}
		assertEquals(10,movieRec.userTopTenMovies(944).size());
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(5)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(15)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(25)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(35)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(45)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(55)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(65)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(85)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(95)));
		assertEquals(true,movieRec.userTopTenMovies(944).contains(movieRec.getMovie(100)));
	}
	
	/**
	 * Testing getting top ten movies
	 */
	@Test
	public void testGetTopTenMovies()
	{
		assertEquals(10,movieRec.getTopTenMovies().size());
		for(Item item : movieRec.getTopTenMovies().values())
		{
			int numberInUsersTop = 0;
			for(User user : movieRec.getUsers().values())
				if(user.getRatings().containsKey(item))
					numberInUsersTop++;
			if(numberInUsersTop < 3)
				fail("Less Than 3 Users Have This Movie In Their Top Ten - "+numberInUsersTop);
		}
	}
	
	/**
	 * Testing the write and read methods
	 * @throws Exception
	 */
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
