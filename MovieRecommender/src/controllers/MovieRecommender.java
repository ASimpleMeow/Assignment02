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

public class MovieRecommender implements RecommenderAPI{

	private Map<Integer,User> users;
	private List<Rating> ratings;
	private Map<Integer,Item> items;
	private int userIDToUse;
	private int itemIDToUse;
	private Serializer serializer;
	private Loader load;
	
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
	
	public void addUser(String firstName, String lastName, int age, char gender, String occupation) {
		User newUser = new User(userIDToUse,firstName,lastName, age, gender,occupation);
		users.put(new Integer(userIDToUse), newUser);
		userIDToUse = users.keySet().size() + 1;
	}

	public void removeUser(int userID) {
		userIDToUse = userID;
		users.remove(userID);
	}

	public void addMovie(String title, String year, String url, List<String> genres) {
		Item newItem = new Item(itemIDToUse,title,year,url,genres);
		items.put(new Integer(itemIDToUse), newItem);
		itemIDToUse = items.keySet().size() + 1;
	}
	
	public void addMovie(String title, String year, String url) {
		
		Item newItem = new Item(itemIDToUse,title,year,url);
		items.put(new Integer(itemIDToUse), newItem);
		itemIDToUse = items.keySet().size() + 1;
	}

	public void addRating(int userID, int movieID, int rating) {
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

	public Item getMovie(int movieID) {
		return items.get(movieID);
	}
	
	public User getUser(int userID)
	{
		return users.get(userID);
	}

	public Map<Item,Rating> getUserRating(int userID) {
		return users.get(userID).getRatings();
	}

	
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
		
		//Putting all the movies into the set 
		//Note, there are movies that the users has already rated in there
		//In the driver they will not be printed
		Iterator<User> it = sim.values().iterator();
		while(it.hasNext())
		{
			User newUser = it.next();
			//Adds the similar users top 10 movies
			recommendations.addAll(userTopTenMovies(newUser.getID()));
		}
		
		return recommendations;
	}
	
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

		return result;
	}

	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		
		serializer.read();
	    users = (Map<Integer, User>) serializer.pop();
	    items = (Map<Integer, Item>) serializer.pop();
	   // ratings = (List<Rating>) serializer.pop();
	}

	public void write() throws Exception{
		
		//serializer.push(ratings);
		serializer.push(items);
	    serializer.push(users);
	    serializer.write(); 
	}

	public Map<Integer, User> getUsers() {
		return users;
	}

	public Map<Integer, Item> getMovies() {
		return items;
	}

	public boolean userExist(int userID) {

		return users.containsKey(userID);
	}

	public boolean itemExist(int itemID) {

		return items.containsKey(itemID);
	}

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
