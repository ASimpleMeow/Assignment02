package controllers;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import models.Item;
import models.User;
import utils.Serializer;
import utils.XMLSerializer;

public class DriverBeta {
	
	private RecommenderAPI movieRec;
	private Scanner input;
	
	public DriverBeta()
	{
		input = new Scanner(System.in);
		File  datastore = new File("datastore.xml");
	    Serializer serializer = new XMLSerializer(datastore);
		movieRec = new MovieRecommender(serializer,datastore.isFile());
		if (datastore.isFile())
	    {
		      try 
		      {
				movieRec.load();
		      }	 
		      catch (Exception e) 
		      {
				e.printStackTrace();
		      }
	    }
	}
	
	public static void main(String[] args) throws Exception
	{
		DriverBeta main = new DriverBeta();
		
	    Shell shell = ShellFactory.createConsoleShell("@", "Welcome to pacemaker-console - ?help for instructions", main);
	    shell.commandLoop();
	    //main.movieRec.write();
	}
	
	@Command(description="Get all users details")
	public void getUsers()
	{
		for(Integer key : movieRec.getUsers().keySet())
			System.out.println(movieRec.getUsers().get(key));
	}
	
	@Command(description="Get all movie details")
	public void getMovies() {
		for(Integer key : movieRec.getMovies().keySet())
			System.out.println(movieRec.getMovies().get(key));	
	}
	
	@Command(description="Get a user by their first and last name")
	public void getUser(@Param(name="first name")String firstName, @Param(name="last name")String lastName)
	{
		System.out.println(movieRec.getUser(firstName,lastName));
	}
	
	@Command(description="Get a movie by its title and release year")
	public void getMovie(@Param(name="title")String title, @Param(name="year")String year)
	{
		System.out.println(movieRec.getMovie(title,year));
	}
	
	@Command(description="Add a new user")
	public User addUser(@Param(name="first name")String firstName,@Param(name="last name")String lastName,
			@Param(name="age")int age,@Param(name="gender")String gender,@Param(name="occupation")String occupation)
	{
		movieRec.addUser(firstName, lastName, age, gender.toUpperCase().charAt(0), occupation);
		System.out.println("\nThe User has been added\n");
		return movieRec.getUser(firstName,lastName);
	}
	
	@Command(description="Add a new user")
	public boolean removeUser(@Param(name="user ID")int userID)
	{
		if(!movieRec.userExist(userID))
		{
			System.err.println("This User ID Doesn't Belong To An Existing User!");
			return false;
		}
		movieRec.removeUser(userID);
		System.out.println("User has been deleted");
		return true;
	}
	
	@Command(description="Add a new movie")
	public Item addMovie(@Param(name="movie title")String title,@Param(name="year of release")String year,
			@Param(name="IMdB URL")String url)
	{
		movieRec.addMovie(title, year, url);
		System.out.println("Movie Added!");
		return movieRec.getMovie(title, year);
	}
	
	@Command(description="Add a new movie with genres")
	public Item addMovieWithGenres(@Param(name="movie title")String title,@Param(name="year of release")String year,
			@Param(name="IMdB URL")String url)
	{
		List<String> genres = new ArrayList<String>();
		System.out.println("Enter '0' when you're done adding genres");
		String genre;
		while(true)
		{
			System.out.print("Genre: ");
			genre = input.next();
			if(genre.equals("0"))
				break;
			genres.add(genre);
		}
		
		movieRec.addMovie(title, year, url,genres);
		System.out.println("Movie Added!");
		return movieRec.getMovie(title, year);
	}
	
	@Command(description="Add a rating from a user for a movie")
	public boolean addRating(@Param(name="user ID")int userID,@Param(name="movie ID")int itemID,
			@Param(name="rating")int rating)
	{
		if(!movieRec.userExist(userID))
		{
			System.err.println("This User ID Doesn't Belong To An Existing User!");
			return false;
		}
		
		if(!movieRec.itemExist(itemID))
		{
			System.err.println("This Movie ID Doesn't Belong To An Existing Movie!");
			return false;
		}
		
		movieRec.addRating(userID, itemID, rating);
		System.out.println("Movie Rated!");
		return true;
	}
	
	@Command (description="Get movie details")
	public void getMovie(@Param(name="movie ID")int itemID) {
		System.out.println(movieRec.getMovie(itemID));
	}
	
	@Command (description="Get user details")
	public void getUser(@Param(name="user ID")int userID) {
		System.out.println(movieRec.getUser(userID));
	}
	
	@Command (description="Get users ratings details")
	public void getUsersRating(@Param(name="user ID")int userID) {
		for(Item item : movieRec.getUserRating(userID).keySet())
			System.out.println("Movie : "+item.getItemTitle()+ "\n"+ movieRec.getUserRating(userID).get(item));
	}
	
	@Command (description="Gets user recommendations within the given limit")
	public void getUsersRecommendations(@Param(name="user ID")int userID,@Param(name="recommendation limit")int limit) {
		Iterator<Item> it = movieRec.getUserRecommendations(userID).iterator();
		while(it.hasNext() && limit>0)
		{
			System.out.println(it.next());
			limit--;
		}
	}
	
	@Command (description="Gets top ten movies of a user")
	public void getUserTopTen(@Param(name="user ID")int userID) {
		for(Item item : movieRec.userTopTenMovies(userID))
			System.out.println(item);
	}
	
	@Command (description="Gets top ten movies by their average ratings")
	public void getTopTen() {
		for(Map.Entry<Integer, Item> mapData : movieRec.getTopTenMovies().entrySet()) 
		{
	        System.out.println("Frequency : " +mapData.getKey()+" Movie : "+mapData.getValue());
	    }
	}
}
