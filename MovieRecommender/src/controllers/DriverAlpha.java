package controllers;

import java.io.File;
import java.io.IOException;
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


public class DriverAlpha {
	
	private Scanner input;
	private RecommenderAPI movieRec;
	
	public static void main(String[] args) throws Exception
	{
		new DriverAlpha();
	}
	
	public DriverAlpha()
	{
		File  datastore = new File("datastore.xml");
	    Serializer serializer = new XMLSerializer(datastore);
		input = new Scanner(System.in);
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
		/*
		for(Integer key : users.keySet())
			System.out.println(users.get(key));
		System.out.println("\n");
		for(Integer key : ratings.keySet())
			System.out.println(ratings.get(key));
		System.out.println("\n");
		for(Integer key : items.keySet())
			System.out.println(items.get(key));
			*/
		runMenu();
	}
	
	
	public int displayMenu()
	{
		System.out.println("MOVIE RECOMMENDER!");
		System.out.println("12) SHOW ALL USERS\n13) SHOW ALL MOVIES");
		System.out.println("1) Add User");
		System.out.println("2) Remove User");
		System.out.println("3) Add Movie");
		System.out.println("4) Add Rating");
		System.out.println("5) Get Movie");
		System.out.println("6) Get Users Rating");
		System.out.println("7) Get Users Recommendations");
		System.out.println("8) Get Top 10 Movies");
		System.out.println("9) Load Save");
		System.out.println("10) Write Save");
		System.out.println("0) Save and Exit");
		
		System.out.print("\n>> ");
		return input.nextInt();
	}
	
	public void runMenu()
	{
		int choice = displayMenu();
		while(choice != 0)
		{
			switch(choice)
			{
			case 1:
				addUser();
			break;
			case 2:
				removeUser();
			break;
			case 12:
				getUsers();
			break;
			case 3:
				addMovie();
			break;
			case 13:
				getMovies();
			break;
			case 4:
				addRating();
			break;
			case 5:
				getMovie();
			break;
			case 6:
				getUsersRating();
			break;
			case 7:
				getUsersRecommendations();
			break;
			case 8:
				getTop10();
			break;
			case 9:
				//Load();
			break;
			case 10:
				//Write();
			break;
			default:
				System.err.println("Wrong Input");
			break;
			}
			
			System.out.println("");
			input.nextLine();
			choice = displayMenu();
		}
		try 
		{
			movieRec.write();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Exiting ...");
		System.exit(0);
	}

	
	private void getMovies() {
		for(Integer key : movieRec.getMovies().keySet())
			System.out.println(movieRec.getMovies().get(key));
		
	}

	private void getUsers() {
		for(Integer key : movieRec.getUsers().keySet())
			System.out.println(movieRec.getUsers().get(key));
	}

	private void getTop10() {
		int limit = 10;
		for(Map.Entry<Integer, Item> mapData : movieRec.getTopTenMovies().entrySet()) 
		{
	        System.out.println("Rating : " +mapData.getKey()+" Movie : "+mapData.getValue());
	        limit--;
	        if(limit == 0)
	        	break;
	    }
	}


	private void getUsersRecommendations() {
		System.out.print("User ID : ");
		int userID = input.nextInt();
		System.out.print("How Many Recommendations Are Needed? : ");
		int limit = input.nextInt();
		Iterator<Item> it = movieRec.getUserRecommendations(userID).iterator();
		while(it.hasNext() && limit>0)
		{
			System.out.println(it.next());
			limit--;
		}
	}

	private void getUsersRating() {
		System.out.print("User ID : ");
		int userID = input.nextInt();
		for(Item item : movieRec.getUserRating(userID).keySet())
			System.out.println("Movie : "+item.getMovieTitle()+ " - "+ movieRec.getUserRating(userID).get(item));
		
	}

	private void getMovie() {
		System.out.print("Movie ID : ");
		int id = input.nextInt();
		System.out.println(movieRec.getMovie(id));
		
	}

	private boolean addRating() {
		User user;
		Item movie;
		System.out.print("Your User ID : ");
		int userID = input.nextInt();
		while(!movieRec.userExist(userID))
		{
			System.err.println("This User doesn't exist!");
			input.nextLine();
			System.out.print("Add A New User?, if not you'll be returned to Main Menu (y/n):");
			char answer = input.next().trim().toLowerCase().charAt(0);
			if(String.valueOf(answer).equals("y"))
				user = addUser();
			else
				return false;
			//System.out.print("Your User ID : ");
			userID = user.getID();//input.nextInt();
			
		}
		System.out.print("The Movie's ID : ");
		int movieID = input.nextInt();
		while(!movieRec.itemExist(movieID))
		{
			System.err.println("This Movie doesn't exist!");
			input.nextLine();
			System.out.print("Add A New Movie?, if not you'll be returned to Main Menu (y/n):");
			char answer = input.next().trim().toLowerCase().charAt(0);
			if(String.valueOf(answer).equals("y"))
				movie = addMovie();
			else
				return false;
			//System.out.print("Your Movie's ID : ");
			movieID = movie.getMovieID();//input.nextInt();
		}
		
		System.out.print("Your Rating : ");
		int rating = input.nextInt();
		while(rating < -5 || rating > 5)
		{
			System.err.println("Rating Must Be Between -5 and 5");
			input.nextLine();
			System.out.print("Your Rating : ");
			rating = input.nextInt();
		}
		
		movieRec.addRating(userID, movieID, rating);
		System.out.println("Movie Rated!");
		//System.out.println(movieRec.getUserRating(userID));
		return true;
	}

	private Item addMovie() {
		input.nextLine();
		System.out.print("Movie Title : ");
		String title = input.nextLine().trim();
		System.out.print("Year of Release : ");
		String year = input.next().trim();
		System.out.print("IMDb URL: ");
		String url = input.next().trim();
		movieRec.addMovie(title, year, url);
		System.out.println("Movie Added!");
		return movieRec.getMovie(title, year);
	}

	private boolean removeUser() {
		System.out.print("User ID to remove : ");
		int id = input.nextInt();
		while(!movieRec.userExist(id))
		{
			System.err.println("This User doesn't exist!\n");
			input.nextLine();
			System.out.print("Return To Main Menu? (y/n):");
			char answer = input.next().trim().toLowerCase().charAt(0);
			if(String.valueOf(answer).equals("y"))
				return false;
			System.out.print("User ID to remove : ");
			id = input.nextInt();
				
		}
		movieRec.removeUser(id);
		System.out.println("User has been deleted");
		return true;
	}

	private User addUser() {
		input.nextLine();
		System.out.print("First Name : ");
		String firstName = input.next().trim();
		System.out.print("Last Name : ");
		String lastName = input.next().trim();
		System.out.print("Age : ");
		int age = input.nextInt();
		System.out.print("(M) of (F) : ");
		char gender = input.next().toUpperCase().trim().charAt(0);
		while(!String.valueOf(gender).equals("F") && !String.valueOf(gender).equals("M"))
		{
			System.err.println("Please Enter A Proper Gender");
			input.nextLine();
			System.out.print("(M) of (F) : ");
			gender = input.next().toUpperCase().trim().charAt(0);
		}
		System.out.print("Occupation : ");
		String occupation = input.next().trim();
		movieRec.addUser(firstName, lastName, age, gender, occupation);
		System.out.println("\nThe User has been added\n");
		return movieRec.getUser(firstName,lastName);
	}
}
