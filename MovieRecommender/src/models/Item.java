package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;

/**
 * Wrapper class for item object
 * 
 * @author Oleksandr Kononov
 *
 */

public class Item implements Serializable{
	
	private static final long serialVersionUID = 554341648233884974L; //Used for Serializer
	private int itemID;
	private String itemTitle;
	private String releaseDate;
	private String url;
	private List<String> genres;
	
	/**
	 * Constructor for the Item class which includes genres
	 * @param itemID
	 * @param itemTitle
	 * @param releaseDate
	 * @param url
	 * @param genres
	 */
	public Item(int itemID, String itemTitle, String releaseDate, String url, List<String> genres)
	{
		this.genres = new ArrayList<String>();
		setItemID(itemID);
		setItemTitle(itemTitle);
		setReleaseDate(releaseDate);
		setUrl(url);
		
		this.genres = genres;
	}
	
	/**
	 * Constructor of the Item class which excludes genres
	 * @param itemID
	 * @param itemTitle
	 * @param releaseDate
	 * @param url
	 */
	public Item(int itemID, String itemTitle, String releaseDate, String url)
	{
		this.genres = new ArrayList<String>();
		setItemID(itemID);
		setItemTitle(itemTitle);
		setReleaseDate(releaseDate);
		setUrl(url);
	}
	
	//SETTERS//
	private void setItemID(int movieID)
	{
		if(movieID < 0)
			throw new IllegalArgumentException();
		this.itemID = movieID;
	}
	
	private void setItemTitle(String movieTitle)
	{
		if(movieTitle == null)
			throw new NullPointerException();
		if(movieTitle.isEmpty())
		{
			System.out.println("ID:" + itemID + "  "+movieTitle);
			throw new IllegalArgumentException();
		}
		this.itemTitle = movieTitle;
	}
	
	private void setReleaseDate(String releaseDate)
	{
		if(releaseDate == null)
			throw new NullPointerException();
		if(releaseDate.isEmpty())
			throw new IllegalArgumentException();
		this.releaseDate = releaseDate;
	}
	
	private void setUrl(String url)
	{
		if(url == null)
			throw new NullPointerException();
		if(url.isEmpty())
			throw new IllegalArgumentException();
		this.url = url;
	}

	//GETTERS//
	public int getItemID() {
		return itemID;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getUrl() {
		return url;
	}

	public List<String> getGenres() {
		return genres;
	}

	/**
	 * ToString method which returns a formated string containing data about the class
	 * @return string
	 */
	@Override
	public String toString() {
		String allGenres = "";
		for(String genre : genres)
			allGenres+= genre + ", ";
		return "movieID: " + itemID + "\nMovie Title: " + itemTitle + "\nRelease Date: " + releaseDate
				+ "\nURL: " + url + "\nGenres: " + allGenres + "\n";
	}
	
	/**
	 * hash code generation for this class, used on HashMaps
	 */
	@Override  
	  public int hashCode()  
	  {  
	     return Objects.hashCode(this.itemTitle, this.releaseDate, this.url,this.genres);  
	  }  
	  
	/**
	 * equals method for this class to compare to other item objects 
	 */
	  @Override
	  public boolean equals(final Object obj)
	  {
	    if (obj instanceof Item)
	    {
	      final Item other = (Item) obj;
	      return Objects.equal(itemTitle, other.itemTitle) 
	          && Objects.equal(releaseDate,  other.releaseDate)
	          && Objects.equal(url,  other.url)
	          && Objects.equal(genres, other.genres);
	    }
	    else
	    {
	      return false;
	    }
	  }
}
