package controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import models.Item;
import models.Rating;
import models.User;

/**
 * Recommender Interface
 * 
 * @author Oleksandr Kononov
 *
 */
public interface RecommenderAPI {
	
	/**
	 * Adds a new user 
	 * @throws Exception 
	 */
	void addUser(String firstName, String lastName, int age, char gender, String occupation) throws Exception;
	
	/**
	 * Removes an existing
	 */
	void removeUser(int userID);
	
	/**
	 * Adds a new movie
	 * @throws Exception 
	 */
	void addMovie(String title, String year, String url) throws Exception;
	
	/**
	 * Adds a new movie with genres
	 * @throws Exception 
	 */
	void addMovie(String title, String year, String url,List<String>genres) throws Exception;
	
	/**
	 * The user will add a rating to the movie
	 * @throws Exception 
	 */
	void addRating(int userID, int movieID, int rating) throws Exception;
	
	/**
	 * Gets a specific movie by id
	 */
	Item getMovie(int movieID);
	
	/**
	 * Gets a specific movie by title and year
	 */
	Item getMovie(String title, String year);
	
	/**
	 * Gets all movies
	 */
	Map<Integer, Item> getMovies();
	
	/**
	 * Gets a user by specific id
	 */
	User getUser(int userID);
	
	/**
	 * Gets a user by first and last name
	 */
	User getUser(String firstName, String lastName);
	
	/**
	 * Gets all users
	 */
	Map<Integer, User> getUsers();
	
	/**
	 * Gets a specific users rating
	 */
	Map<Item, Rating> getUserRating(int userID);
	
	/**
	 * Gets a user recommendations
	 */
	HashSet<Item> getUserRecommendations(int userID);
	
	/**
	 * Gets a user specific top ten movies
	 */
	List<Item> userTopTenMovies(int userID);
	
	/**
	 * Get the top ten movies that most users rated highly
	 */
	Map<Integer, Item> getTopTenMovies();
	
	
	/**
	 * Returns a boolean if the user exists, checks by userID
	 */
	boolean userExist(int userID);
	
	/**
	 * Returns a boolean if the item exists, checks by itemID
	 */
	boolean itemExist(int itemID);
	
	/**
	 * Loads data from file
	 * @throws Exception
	 */
	void load() throws Exception;
	
	/**
	 * Writes data to file
	 * @throws Exception 
	 */
	void write() throws Exception;
}
