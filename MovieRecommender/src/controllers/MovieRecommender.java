package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
		        		items.get(ratings.get(i).getItemID()).addRating(ratings.get(i));
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
		
		HashSet<Item> recommendations = new HashSet <Item>();
		
		HashMap<Item,Rating> currentUserRatings = users.get(userID).getRatings();
		User currentUser = users.get(userID);
		
		int[] similarity = new int[users.size()+1];//Doesn't use index at 0
		TreeMap<Integer,User> sim = new TreeMap<Integer,User>(new Comparator<Integer>(){

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2.intValue()-o1.intValue();
			}
		});
		
		for(User user : users.values())
		{
			if(!user.equals(currentUser))
			{
				HashMap<Item,Rating> thisUser = user.getRatings();
				for(Item currentUserItem : currentUserRatings.keySet())
				{
					if(thisUser.containsKey(currentUserItem))
					{
						similarity[user.getID()]+=(thisUser.get(currentUserItem).getRating() * currentUserRatings.get(currentUserItem).getRating());
					}
				}
				sim.put(similarity[user.getID()],user);
			}
		}
		
		for(User user : sim.values())
		{
			for(Item item : user.getRatings().keySet())
			{
				if(!(currentUser.getRatings().containsKey(item)))
				{
					recommendations.add(item);
				}
			}
		}
		return recommendations;
	}
	
	public List<Item> userTopTenMovies(int userID)
	{
		int forLoopLimit = 10;
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
		
		if(ratings.size() < 10)
			forLoopLimit = ratings.size();		
		
		for(int i=0;i<forLoopLimit;i++)
		{
			result.add(items.get(ratings.get(i).getItemID()));
			//System.out.println(items.get(ratings.get(i)));
		}
		
		return result;
	}

	public Map<Integer,Item> getTopTenMovies() {
		
		Map<Integer,Item> result = new TreeMap<Integer,Item>(new Comparator<Integer>() 
	     {
	        @Override
	        public int compare(Integer o1, Integer o2) {                
	            return o2.compareTo(o1);
	         }
	     });
		
		for(Item item : items.values())
		{
			result.put(item.getAvgRating(),item);
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
			if (currentItem.getMovieTitle().equals(title) && currentItem.getReleaseDate().equals(year))
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
