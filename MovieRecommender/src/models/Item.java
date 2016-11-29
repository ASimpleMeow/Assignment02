package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

public class Item implements Serializable{
	
	private static final long serialVersionUID = 554341648233884974L;
	private int movieID;
	private String movieTitle;
	private String releaseDate;
	private String url;
	private List<String> genres;
	private Map<Integer,Rating> ratings;
	
	public Item(int movieID, String movieTitle, String releaseDate, String url, List<String> genres)
	{
		this.genres = new ArrayList<String>();
		setMovieID(movieID);
		setMovieTitle(movieTitle);
		setReleaseDate(releaseDate);
		setUrl(url);
		
		this.genres = genres;
		ratings = new HashMap<Integer,Rating>();
	}
	
	public Item(int movieID, String movieTitle, String releaseDate, String url)
	{
		this.genres = new ArrayList<String>();
		setMovieID(movieID);
		setMovieTitle(movieTitle);
		setReleaseDate(releaseDate);
		setUrl(url);
	}
	
	private void setMovieID(int movieID)
	{
		if(movieID < 0)
			throw new IllegalArgumentException();
		this.movieID = movieID;
	}
	
	private void setMovieTitle(String movieTitle)
	{
		if(movieTitle == null)
			throw new NullPointerException();
		if(movieTitle.isEmpty())
		{
			System.out.println("ID:" + movieID + "  "+movieTitle);
			throw new IllegalArgumentException();
		}
		this.movieTitle = movieTitle;
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
	
	public void addRating(Rating rating) {
		ratings.put(rating.getUserID(),rating);
	}
	
	public Map<Integer,Rating> getRatings()
	{
		return ratings;
	}

	public int getMovieID() {
		return movieID;
	}

	public String getMovieTitle() {
		return movieTitle;
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
	
	public int getAvgRating()
	{
		int avgRating = 0;
		
		for(Rating rating : ratings.values())
			avgRating+= rating.getRating();
		return avgRating;
	}

	@Override
	public String toString() {
		String allGenres = "";
		for(String genre : genres)
			allGenres+= genre + " ";
		return "Item [movieID=" + movieID + ", movieTitle=" + movieTitle + ", releaseDate=" + releaseDate
				+ ", url=" + url + ", genres=" + allGenres + "]";
	}
	
	@Override  
	  public int hashCode()  
	  {  
	     return Objects.hashCode(this.movieTitle, this.releaseDate, this.url,this.genres);  
	  }  
	  
	  @Override
	  public boolean equals(final Object obj)
	  {
	    if (obj instanceof Item)
	    {
	      final Item other = (Item) obj;
	      return Objects.equal(movieTitle, other.movieTitle) 
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
