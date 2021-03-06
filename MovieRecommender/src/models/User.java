package models;

import java.io.Serializable;
import java.util.HashMap;

import com.google.common.base.Objects;

/**
 * Wrapper class for user object
 * 
 * @author Oleksandr Kononov
 *
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 2191424049952121573L;//Used for Serailizer
	private int id;
	private String firstName;
	private String lastName;
	private int age;
	private char gender;
	private Occupation occupation;
	private HashMap<Item,Rating> ratings;
	
	/**
	 * Constructor for the User class
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param occupation
	 */
	public User(int id,String firstName, String lastName, int age, char gender, String occupation)
	{
		setUserID(id);
		setFirstName(firstName);
		setLastName(lastName);
		setAge(age);
		setGender(gender);
		setOccupation(occupation);
		ratings = new HashMap<Item,Rating>();
	}
	
	/**
	 * Adds a rating to the user
	 * @param item
	 * @param rating
	 */
	public void addRating(Item item,Rating rating) {
		ratings.put(item, rating);
	}
	
	//GETTERS//
	public HashMap<Item,Rating> getRatings()
	{
		return ratings;
	}
	
	public int getID() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getAge() {
		return age;
	}

	public char getGender() {
		return gender;
	}

	public Occupation getOccupation() {
		return occupation;
	}


	/**
	 * ToString method which returns a formated string containing data about the class
	 * @return string
	 */
	@Override
	public String toString() {
		return "User id: " + id + "\nFirst Name: "+firstName+"\nLast Name: "+lastName+"\nAge: " + age+"\nGender: " + gender + 
				"\nOccupation: " + occupation+"\n";
	}
	
	/**
	 * hash code generation for this class, used on HashMaps
	 */
	@Override  
	  public int hashCode()  
	  {  
	     return Objects.hashCode(this.lastName, this.firstName, this.age, this.gender,this.occupation);  
	  }  
	  
	/**
	 * equals method for this class to compare to other item objects 
	 */
	  @Override
	  public boolean equals(final Object obj)
	  {
	    if (obj instanceof User)
	    {
	      final User other = (User) obj;
	      return Objects.equal(firstName, other.firstName) 
	          && Objects.equal(lastName,  other.lastName)
	          && Objects.equal(age,     other.age)
	          && Objects.equal(gender,  other.gender)
	          && Objects.equal(occupation, other.occupation);
	    }
	    else
	    {
	      return false;
	    }
	  }
	  
	  //SETTERS//
	  private void setUserID(int id)
		{
			if(id < 0)
				throw new IllegalArgumentException();
			this.id = id;
		}
		
		private void setFirstName(String firstName)
		{
			if(firstName == null)
				throw new NullPointerException();
			if(firstName.isEmpty())
				throw new IllegalArgumentException();
			this.firstName = firstName;
		}
		
		private void setLastName(String lastName)
		{
			if(lastName == null)
				throw new NullPointerException();
			if(lastName.isEmpty())
				throw new IllegalArgumentException();
			this.lastName = lastName;
		}
		
		private void setAge(int age)
		{
			if(age < 1 || age > 120)
				throw new IllegalArgumentException();
			this.age = age;
		}
		
		private void setGender(char gender)
		{
			if(!(String.valueOf(gender).equals("F") || String.valueOf(gender).equals("M")))
				throw new IllegalArgumentException();
			this.gender = gender;
		}
		
		private void setOccupation(String occupation)
		{
			if(occupation == null || occupation.isEmpty())
			{
				this.occupation = Occupation.NONE;
			}
			else
			{
				for(Occupation o : Occupation.values())
				{
					if(o.toString().equals(occupation.trim().toUpperCase()))
					{
						this.occupation = o;
						break;
					}
				}
				if(this.occupation == null)
					this.occupation = Occupation.OTHER;
			}
		}
}
