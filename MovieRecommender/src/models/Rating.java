package models;

import java.io.Serializable;

import com.google.common.base.Objects;

public class Rating implements Comparable<Rating>, Serializable{
	
	private static final long serialVersionUID = -6796550095611787611L;
	private int userID;
	private int itemID;
	private int rating;
	private double timestamp;
	
	public Rating(int userID, int itemID, int rating,double timestamp)
	{
		setUserID(userID);
		setItemID(itemID);
		setRating(rating);
		this.timestamp = timestamp;
	}
	
	public Rating(int userID, int itemID, int rating)
	{
		setUserID(userID);
		setItemID(itemID);
		setRating(rating);
	}
	
	public double getTimestamp()
	{
		return timestamp;
	}
	
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

	public int getUserID() {
		return userID;
	}

	public int getItemID() {
		return itemID;
	}

	public int getRating() {
		return rating;
	}

	@Override
	public String toString() {
		return "Rating [userID=" + userID + ", itemID=" + itemID + ", rating=" + rating + "]";
	}
	
	@Override  
	  public int hashCode()  
	  {  
	     return Objects.hashCode(this.itemID, this.rating);  
	  }  
	  
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


	@Override
	public int compareTo(Rating o) {
		return (int) (o.timestamp - o.getTimestamp());
	}
}
