package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.introcs.In;
import models.Item;
import models.Rating;
import models.User;

/**
 * Loader class will load data from files provided
 * 
 * @author Oleksandr Kononov
 *
 */
public class Loader {
	
	private Map<Integer,String> genres;
	private int userID = 1;
	private int itemID = 1;
	
	/**
	 * Constructor for the class will start off by loading the genres
	 */
	public Loader()
	{
		try {
			loadGenres("data_movieLens/genre.dat");
		} catch (Exception e) {
			System.err.println("Error Loading Genres");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the genres from file
	 * @param location
	 * @throws Exception
	 */
	private void loadGenres(String location) throws Exception
	{
		genres = new HashMap<Integer,String>();
		File genreFile = new File(location);
        In inGenre = new In(genreFile);
          //each field is separated(delimited) by a '|'
        String delims = "[|]";
        while (!inGenre.isEmpty()) {
            // get genre and rating from data source
            String genresDetails = inGenre.readLine();

            // parse genre details string
            String[] genreTokens = genresDetails.split(delims);

            if (genreTokens.length == 2) {
                genres.put(new Integer(genreTokens[1]), genreTokens[0]);
            }else
            {
                throw new Exception("Invalid member length: "+genreTokens.length);
            }
        }
	}
	
	/**
	 * Load in the users from file
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public Map<Integer,User> loadUsers(String location) throws Exception
	{
		Map<Integer,User> users = new HashMap<Integer,User>();
		File usersFile = new File(location);
        In inUsers = new In(usersFile);
          //each field is separated(delimited) by a '|'
        String delims = "[|]";
        while (!inUsers.isEmpty()) {
            // get user and rating from data source
            String userDetails = inUsers.readLine();

            // parse user details string
            String[] userTokens = userDetails.split(delims);
            
            if (userTokens.length == 7) {
                try
                {
                	users.put(userID,new User(userID,userTokens[1],userTokens[2],Integer.parseInt(userTokens[3]),userTokens[4].charAt(0),userTokens[5]));
                    userID++;
                }catch(IllegalArgumentException e)
                {
                	System.err.println("Couldn't add UserID: "+userID+"\nDue to Illegal Arguments");
                }
                catch(Exception e)
                {
                	System.err.println("Couldn't add UserID: "+userID+"\nDue to unknown error");
                }
            }else
            {
                throw new Exception("Invalid member length: "+userTokens.length);
            }
        }
        return users;
	}
	
	/**
	 * Loads in ratings from file
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public List<Rating> loadRatings(String location) throws Exception
	{
		List<Rating> ratings = new ArrayList<Rating>();
		File ratingsFile = new File(location);
        In inRating = new In(ratingsFile);
          //each field is separated(delimited) by a '|'
        String delims = "[|]";
        while (!inRating.isEmpty()) {
            // get rating and rating from data source
            String ratingDetails = inRating.readLine();

            // parse rating details string
            String[] ratingTokens = ratingDetails.split(delims);

            if (ratingTokens.length == 4) {
            	try
            	{
            		Rating rating = new Rating(Integer.parseInt(ratingTokens[0]),Integer.parseInt(ratingTokens[1]),Integer.parseInt(ratingTokens[2]),Double.parseDouble(ratingTokens[3]));
                	ratings.add(rating);
            	}catch(IllegalArgumentException e)
            	{
            		System.err.println("Couldn't add rating for userID: "+Integer.parseInt(ratingTokens[0])+" to MovieID: "+ Integer.parseInt(ratingTokens[1])+
            				"\nDue to Illegal Arguments");
            	}
            	catch(Exception e)
            	{
            		System.err.println("Couldn't add rating for userID: "+Integer.parseInt(ratingTokens[0])+" to MovieID: "+ Integer.parseInt(ratingTokens[1])+
            				"\nDue to unknown error");
            	}
            }else
            {
                throw new Exception("Invalid member length: "+ratingTokens.length);
            }
        }
        Collections.sort(ratings);//Sort by their timestamp
        return ratings;
	}
	
	/**
	 * Load in the items from file
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public Map<Integer,Item> loadItems(String location) throws Exception
	{
		Map<Integer,Item> items = new HashMap<Integer,Item>();
		File itemsFile = new File(location);
        In inItems = new In(itemsFile);
          //each field is separated(delimited) by a '|'
        String delims = "[|]";
        while (!inItems.isEmpty()) {
            // get item and rating from data source
            String itemsDetails = inItems.readLine();

            // parse item details string
            String[] itemTokens = itemsDetails.split(delims);

            if (itemTokens.length == 23) {
            	//index 4 to index 22 is the genres
            	
            	List<String> itemGenres = new ArrayList<String>();
            	for(int i=4;i<itemTokens.length;i++)
            	{
            		if(Integer.parseInt(itemTokens[i]) == 1)
            		{
            			itemGenres.add(genres.get(i-4));
            		}
            	}
            	try
            	{
            		items.put(itemID,new Item(itemID,itemTokens[1],itemTokens[2],itemTokens[3],itemGenres));
            		itemID++;
            	}
            	catch(IllegalArgumentException e)
            	{
            		System.err.println("Couldn't add item!\nID: "+itemID+"\nTitle: "+itemTokens[1]+"\nDue to Illegal Argument");
            	}
            	catch(Exception e)
            	{
            		System.err.println("Couldn't add item!\nID: "+itemID+"\nTitle: "+itemTokens[1]+"\nDue to unknown error");
            	}
            }else
            {
                throw new Exception("Invalid member length: "+itemTokens.length);
            }
        }
        return items;
	}
}
