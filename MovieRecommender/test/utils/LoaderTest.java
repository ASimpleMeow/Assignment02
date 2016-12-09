package utils;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Item;
import models.Rating;
import models.User;

public class LoaderTest {

	private Loader load;
	
	@Before
	public void setup()
	{
		load = new Loader();
	}
	
	@After
	public void tearDown()
	{
		load = null;
	}
	
	@Test
	public void testLoadUsers() throws Exception {
		Map<Integer,User> users = load.loadUsers("data_movieLens/users.dat");
		assertEquals(943,users.size(),0.01);
		assertEquals("Leonard",users.get(1).getFirstName());
		assertEquals(1,users.get(1).getID());
		assertEquals(943,users.get(943).getID());
		assertEquals("Noel",users.get(943).getFirstName());
		
		assertNotEquals(users.get(1),users.get(2));
		assertNotEquals(users.get(1),users.get(943));
	}
	
	@Test
	public void testLoadItems() throws Exception {
		Map<Integer,Item> items = load.loadItems("data_movieLens/items.dat");
		//There are 3 invalid items in the items.dat file
		assertEquals(1679,items.size(),0.01);
		assertEquals("Toy Story (1995)",items.get(1).getItemTitle());
		assertEquals(1,items.get(1).getItemID());
		assertEquals(1679,items.get(1679).getItemID());
		assertEquals("Scream of Stone (Schrei aus Stein) (1991)",items.get(1679).getItemTitle());
		
		assertNotEquals(items.get(1),items.get(2));
		assertNotEquals(items.get(1),items.get(1679));
		
		//Testing Genres of Items
		assertEquals(3,items.get(1).getGenres().size(),0.01);
		assertEquals("Animation",items.get(1).getGenres().get(0));
		assertEquals("Children's",items.get(1).getGenres().get(1));
		assertEquals("Comedy",items.get(1).getGenres().get(2));
		assertEquals(1,items.get(1679).getGenres().size(),0.01);
		assertEquals("Drama",items.get(1679).getGenres().get(0));
	}

	@Test
	public void testLoadRatings() throws Exception
	{
		List<Rating> ratings = load.loadRatings("data_movieLens/ratings.dat");
		assertEquals(100000,ratings.size(),0.01);
		assertNotEquals(ratings.get(0),ratings.get(1));
		assertNotEquals(ratings.get(0),ratings.get(99999));
	}
}
