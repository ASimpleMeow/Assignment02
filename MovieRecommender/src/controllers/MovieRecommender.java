package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import models.Item;
import models.Rating;
import models.User;
import utils.Loader;
import utils.Serializer;

/**
 * The MovieRecommender class contains all implementations of the 
 * RecommenderAPI
 * @author Oleksandr Kononov
 *
 */
public class MovieRecommender implements RecommenderAPI{

	private Map<Integer,User> users; //UserID to User map
	private List<Rating> ratings; //List ratings
	private Map<Integer,Item> items; //ItemID to Item map
	private int userIDToUse; //The next ID to use when adding a new user
	private int itemIDToUse; //The next ID to use when adding a new item
	private Serializer serializer;//Serializer
	private Loader load;//Loader to load in data from cvs
	
	/**
	 * Contructor for the class handles initiallization and data loading
	 * @param serializer
	 * @param hasSave
	 */
	public MovieRecommender(Serializer serializer,boolean hasSave)
	{
		this.serializer = serializer;
		users = new HashMap<Integer,User>();
		ratings = new ArrayList<Rating>();
		items = new HashMap<Integer,Item>();
		if(!hasSave)
		{
			try
			{
				load = new Loader();
				users = load.loadUsers("data_movieLens/users.dat");
				items = load.loadItems("data_movieLens/items.dat");
				ratings = load.loadRatings("data_movieLens/ratings.dat");
				for(int i=0;i<ratings.size();i++)
		        {
		        	if(userExist(ratings.get(i).getUserID()) && itemExist(ratings.get(i).getItemID()))
		        	{
		        		//Since the ratings are sorted in descending order of time
		        		//HashMaps don't allow duplicate keys
		        		users.get(ratings.get(i).getUserID()).addRating(items.get(ratings.get(i).getItemID()), ratings.get(i));
		        	}
		        }
			}
			catch(Exception e)
			{
				System.err.println("Error Loading");
				e.printStackTrace();
			}
			
			
			userIDToUse = users.keySet().size() + 1;
			itemIDToUse = items.keySet().size() + 1;
		}
	}
	
	/**
	 * Adds a new user
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param occupation
	 * @throws Exception
	 */
	public void addUser(String firstName, String lastName, int age, char gender, String occupation) throws Exception{
		User newUser = new User(userIDToUse,firstName,lastName, age, gender,occupation);
		users.put(new Integer(userIDToUse), newUser);
		userIDToUse = users.keySet().size() + 1;
	}

	/**
	 * Removes an existing user by ID
	 * @param userID
	 */
	public void removeUser(int userID) {
		userIDToUse = userID;
		users.remove(userID);
	}

	/**
	 * Adds a new Item (movie) with genres
	 * @param title
	 * @param year
	 * @param url
	 * @param genres
	 * @throws Exception
	 */
	public void addMovie(String title, String year, String url, List<String> genres) throws Exception 
	{
		Item newItem = new Item(itemIDToUse,title,year,url,genres);
		items.put(new Integer(itemIDToUse), newItem);
		itemIDToUse = items.keySet().size() + 1;
	}
	
	/**
	 * Adds a new Item (movie) without genres
	 * @param title
	 * @param year
	 * @param url
	 * @throws Exception
	 */
	public void addMovie(String title, String year, String url) throws Exception  
	{
		
		Item newItem = new Item(itemIDToUse,title,year,url);
		items.put(new Integer(itemIDToUse), newItem);
		itemIDToUse = items.keySet().size() + 1;
	}

	/**
	 * Adds a new rating
	 * @param userID
	 * @param movieID
	 * @param rating
	 * @throws Exception
	 */
	public void addRating(int userID, int movieID, int rating) throws Exception{
		Rating newRating = new Rating(userID,movieID,rating);
		if( !(users.get(userID).getRatings().containsKey(items.get(movieID))) )
		{
			users.get(userID).addRating(items.get(movieID), newRating);
		}
		else
		{
			users.get(userID).getRatings().replace(items.get(movieID), newRating);
		}
	}

	/**
	 * Gets an Item by ID
	 * @param movieID
	 * @return Item
	 */
	public Item getMovie(int movieID) {
		return items.get(movieID);
	}
	
	/**
	 * Gets a User by ID
	 * @param userID
	 * @return user
	 */
	public User getUser(int userID)
	{
		return users.get(userID);
	}

	/**
	 * Returns users ratings
	 * @param userID
	 * @return item to rating map
	 */
	public Map<Item,Rating> getUserRating(int userID) {
		return users.get(userID).getRatings();
	}

	/**
	 * Returns users recommendations
	 * @param userID
	 * @return set of items
	 */
	public HashSet<Item> getUserRecommendations(int userID) {
		
		//Makes a recommendations set of Items (Movies)
		HashSet<Item> recommendations = new HashSet <Item>();
		
		//The current Users ratings on Items 
		HashMap<Item,Rating> currentUserRatings = users.get(userID).getRatings();
		User currentUser = users.get(userID);
		
		//TreeMap of Similarity To User will be sorted in descending order
		//Thus sim will contain all users that are similar to the currentUser by ratings in
		//descending order
		TreeMap<Integer,User> sim = new TreeMap<Integer,User>(new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.intValue()-o1.intValue();
			}
		});
		
		//Will loop through all users (excludes the currentUser)
		for(User user : users.values())
		{
			if(!user.equals(currentUser))
			{
				int similarity = 0;
				HashMap<Item,Rating> thisUser = user.getRatings();
				for(Item currentUserItem : currentUserRatings.keySet())
				{
					if(thisUser.containsKey(currentUserItem))
					{
						//Dot product of the ratings of two users on the same item (movie)
						similarity+=(thisUser.get(currentUserItem).getRating() * currentUserRatings.get(currentUserItem).getRating());
					}
				}
				sim.put(similarity,user);
			}
		}
		
		//Putting all the movies which the user hasn't already rated into 
		//the set
		Iterator<User> it = sim.values().iterator();
		while(it.hasNext())
		{
			User newUser = it.next();
			//Adds the similar users top 10 movies
			for(Item item : userTopTenMovies(newUser.getID()))
			{
				if(!currentUserRatings.containsKey(item))
					recommendations.add(item);
			}
		}
		
		return recommendations;
	}
	
	/**
	 * Returns a user specific top ten items (movies)
	 * @param userID
	 * @return list of items
	 */
	public List<Item> userTopTenMovies(int userID)
	{
		List<Rating> ratings = new ArrayList<Rating>();
		List<Item> result = new ArrayList<Item>();
		for(Rating rating : users.get(userID).getRatings().values())
		{
			ratings.add(rating);
		}
		Collections.sort(ratings,new Comparator<Rating>(){
			
			@Override
			public int compare(Rating o1,Rating o2)
			{
				return o2.getRating()-o1.getRating();
			}
		});	
		
		for(Rating rating : ratings)
		{
			if(result.size() >= 10)
				break;
			result.add(items.get(rating.getItemID()));
		}
		return result;
	}

	/**
	 * Returns top ten movies
	 * @return frequency of item occurrence in user specific top tens to item map
	 */
	public Map<Integer,Item> getTopTenMovies() 
	{
		//Frequency of Item to the Item in descending order of frequency
		TreeMap<Integer,Item> result = new TreeMap<Integer,Item>(new Comparator<Integer>()
				{

					@Override
					public int compare(Integer o1, Integer o2) {
						return o2.intValue()-o1.intValue();
					}
				});
		//A list of all users' top ten movies
		List<Item> allTopTens = new ArrayList<Item>();
		
		//Adding all the top ten movies to the allTopTens
		for(User user : users.values())
		{
			allTopTens.addAll(userTopTenMovies(user.getID()));
		}
		
		//Removing the duplicates
		Set<Item> itemSet = new HashSet<Item>(allTopTens);
		
		//Adding the movies and the frequency of the duplicates
		for(Item item : itemSet)
		{
			//Collections.frequency counts the frequency of the current item in the set
			result.put(Collections.frequency(allTopTens, item),item);
		}
		//Getting the SubMap
		int index = 0;
		int toKey = 0;
		for(Integer i : result.keySet())
		{
			if(index == 10)
				toKey = i;
			index++;
		}
		return result.subMap(result.firstKey(), toKey);
	}

	/**
	 * Loads data from xml file
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		
		serializer.read();
	    users = (Map<Integer, User>) serializer.pop();
	    items = (Map<Integer, Item>) serializer.pop();
	}

	/**
	 * Writes data to xml file
	 * @throws Exception
	 */
	public void write() throws Exception{
		
		serializer.push(items);
	    serializer.push(users);
	    serializer.write(); 
	}

	/**
	 * Returns all users
	 * @return userID to user map
	 */
	public Map<Integer, User> getUsers() {
		return users;
	}

	/**
	 * Return all items
	 * @return itemID to item map
	 */
	public Map<Integer, Item> getMovies() {
		return items;
	}

	/**
	 * Checks if the user exists
	 * @param userID
	 * @return boolean
	 */
	public boolean userExist(int userID) {

		return users.containsKey(userID);
	}

	/**
	 * Checks if the item exists
	 * @param itemID
	 * @return boolean
	 */
	public boolean itemExist(int itemID) {

		return items.containsKey(itemID);
	}

	/**
	 * Gets item by title and year
	 * @param title
	 * @param year
	 * @return item
	 */
	public Item getMovie(String title, String year) {
		Iterator<Integer> it = items.keySet().iterator();
		Item currentItem;
		while(it.hasNext())
		{
			currentItem = items.get(it.next());
			if (currentItem.getItemTitle().equals(title) && currentItem.getReleaseDate().equals(year))
			{
				return currentItem;
			}
		}
		return null;
	}

	/**
	 * Gets user by first and last name
	 * @param firstName
	 * @param lastName
	 * @return user
	 */
	public User getUser(String firstName, String lastName) {
		Iterator<Integer> it = users.keySet().iterator();
		User currentUser;
		while(it.hasNext())
		{
			currentUser = users.get(it.next());
			if (currentUser.getFirstName().equals(firstName) && currentUser.getLastName().equals(lastName))
			{
				return currentUser;
			}
		}
		return null;
	}
}
