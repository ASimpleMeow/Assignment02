package models;

import com.google.common.base.Objects;

public class Genre {
	
	private int genreIndex;
	private String genre;
	
	public Genre(int genreIndex , String genre)
	{
		setIndex(genreIndex);
		setGenre(genre);
	}
	
	private void setIndex(int genreIndex)
	{
		if(genreIndex < 0)
			throw new IllegalArgumentException();
		this.genreIndex = genreIndex;
	}
	
	private void setGenre(String genre)
	{
		if(genre == null)
			throw new NullPointerException();
		if(genre.isEmpty())
			throw new IllegalArgumentException();
		this.genre = genre;
	}

	public int getGenreIndex() {
		return genreIndex;
	}

	public String getGenre() {
		return genre;
	}
	
	@Override
	  public boolean equals(final Object obj)
	  {
	    if (obj instanceof Genre)
	    {
	      final Genre other = (Genre) obj;
	      return Objects.equal(genreIndex, other.genreIndex) 
	          && Objects.equal(genre,  other.genre);
	    }
	    else
	    {
	      return false;
	    }
	  }
}
