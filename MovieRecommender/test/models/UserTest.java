package models;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class UserTest {

	User testUser;
	
	@Before
	public void setup()
	{
		testUser = new User(0,"Oleksandr","Kononov",19,'M',"Programmer");
	}
	
	@After
	public void tearDown()
	{
		testUser = null;
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailID() {
		new User(-1,"Test1","Test2",19,'M',"Programmer");
		fail();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailFirstName1() {
		new User(1,"","Test2",19,'M',"Programmer");
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorFailFirstName2() {
		new User(1,null,"Test2",19,'M',"Programmer");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailLastName1() {
		new User(1,"Test1","",19,'M',"Programmer");
	}
	
	@Test (expected = NullPointerException.class)
	public void testContructorFailLastName2() {
		new User(1,"Test1",null,19,'M',"Programmer");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailAge1() {
		new User(1,"Test1","Test2",0,'M',"Programmer");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailAge2() {
		new User(1,"Test1","Test2",150,'M',"Programmer");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testContructorFailGender() {
		new User(1,"Test1","Test2",19,'m',"Programmer");
	}
	
	@Test
	public void testContructor()
	{
		User user1 = new User(0,"Test1","Test2",100,'F',null);
		User user2 = new User(10,"Test3","Test4",45,'M',"");
		User user3 = new User(5,"Test5","Test6",2,'F',"Tester");
		User user4 = new User(10,"Test3","Test4",45,'M',"");
		User user5 = user1;
		
		assertEquals(user1.getOccupation(),Occupation.NONE);
		assertEquals(user2.getOccupation(),Occupation.NONE);
		assertEquals(user3.getOccupation(),Occupation.OTHER);
		assertEquals(user4,user2);
		assertSame(user5,user1);
	}
	
	@Test
	public void testGetters()
	{
		assertEquals(0,testUser.getID());
		assertEquals("Oleksandr",testUser.getFirstName());
		assertEquals("Kononov",testUser.getLastName());
		assertEquals(19,testUser.getAge());
		assertEquals('M',testUser.getGender());
		assertEquals(Occupation.PROGRAMMER,testUser.getOccupation());
	}
	
	@Test
	public void testToString()
	{
		assertEquals("User [id=0, firstName=Oleksandr, lastName=Kononov, age=19, gender=M, occupation=PROGRAMMER]",testUser.toString());
	}

}
