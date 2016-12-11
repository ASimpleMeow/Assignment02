package models;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * Wrapper class for rating object
 * 
 * @author Oleksandr Kononov
 *
 */
public class Rating implements Comparable<Rating>, Serializable{
	
	private static final long serialVersionUID = -6796550095611787611L;//Used for Serailizer
	private int userID;
	private int itemID;
	private int rating;
	private double timestamp;
	
	/**
	 * Constructor for the Rating class that includes timestamp
	 * used only for first time loading to remove duplicate ratings
	 * @param userID
	 * @param itemID
	 * @param rating
	 * @param timestamp
	 */
	public Rating(int userID, int itemID, int rating,double timestamp)
	{
		setUserID(userID);
		setItemID(itemID);
		setRating(rating);
		this.timestamp = timestamp;
	}
	
	/**
	 * Standard constructor for the Rating class
	 * @param userID
	 * @param itemID
	 * @param rating
	 */
	public Rating(int userID, int itemID, int rating)
	{
		setUserID(userID);
		setItemID(itemID);
		setRating(rating);
	}
	
	//SETTERS//
	private void setUserID(int userID)
	{
		if(userID < 0)
			throw new IllegalArgumentException("User ID must not be negative");
		this.userID = userID;
	}
	
	private void setItemID(int itemID)
	{
		if(itemID < 0)
			throw new IllegalArgumentException("Movie ID must not be negative");
		this.itemID = itemID;
	}
	
	private void setRating(int rating)
	{
		if(rating > 5 || rating < -5)
			throw new IllegalArgumentException("Rating must be between -5 and 5 (inclusive)");
		this.rating = rating;
	}

	//GETTERS//
	public double getTimestamp()
	{
		return timestamp;
	}
	
	public int getUserID() {
		return userID;
	}

	public int getItemID() {
		return itemID;
	}

	public int getRating() {
		return rating;
	}

	/**
	 * ToString method which returns a formated string containing data about the class
	 * @return string
	 */
	@Override
	public String toString() {
		return "Rating\nUser ID: " + userID + "\nMovie ID: " + itemID + "\nRating:" + rating + "\n";
	}
	
	/**
	 * hash code generation for this class, used on HashMaps
	 */
	@Override  
	  public int hashCode()  
	  {  
	     return Objects.hashCode(this.itemID, this.rating);  
	  }  
	  
	/**
	 * equals method for this class to compare to other item objects 
	 */
	  @Override
	  public boolean equals(final Object obj)
	  {
	    if (obj instanceof Rating)
	    {
	      final Rating other = (Rating) obj;
	      return Objects.equal(itemID, other.itemID) 
	          && Objects.equal(rating,  other.rating);
	    }
	    else
	    {
	      return false;
	    }
	  }


	 /**
	  * Comparable method to compare one rating to another
	  */
	@Override
	public int compareTo(Rating o) {
		return (int) (o.timestamp - o.getTimestamp());
	}
}
