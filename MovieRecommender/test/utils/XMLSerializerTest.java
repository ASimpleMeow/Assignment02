package utils;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.EmptyStackException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Item;
import models.User;

/**
 * JUNIT Test for XMLSerializer class
 * @author Oleksandr Kononov
 *
 */

public class XMLSerializerTest {
	
	Serializer s;
	
	/**
	 * Runs before every test
	 */
	@Before
	public void setup()
	{
		s = new XMLSerializer(new File("test.xml"));
	}
	
	/**
	 * Runs after every test
	 */
	@After
	public void tearDown()
	{
		s = null;
		File testFile = new File("test.xml");
		if(testFile.exists())
			testFile.delete();
	}
	
	/**
	 * Testing serailizing
	 * @throws Exception
	 */
	@Test
	public void testXMLSerializer() throws Exception {
		String testString = "Test";
		User testUser = new User(1,"TestName","TestLastName",19,'M',"NONE");
		Item testItem = new Item(2,"ItemTitle","2016","www.none.com",new ArrayList<String>());
		User testUser2 = new User(3,"TestName1","TestLastName1",19,'M',"NONE");
		User testUser3 = new User(4,"TestName2","TestLastName2",19,'M',"NONE");
		
		s.push(testString);
		s.push(testUser);
		s.push(testItem);
		s.push(testUser2);
		s.push(testUser3);
		
		s.write();
		
		assertEquals(testUser3,s.pop());
		assertEquals(testUser2,s.pop());
		assertEquals(testItem,s.pop());
		assertEquals(testUser,s.pop());
		assertEquals(testString,s.pop());
		
		try
		{
			s.pop();
			fail("Stack not empty");
		}
		catch(EmptyStackException e)
		{
			
		}
		
		s.read();
		
		assertEquals(testUser3,s.pop());
		assertEquals(testUser2,s.pop());
		assertEquals(testItem,s.pop());
		assertEquals(testUser,s.pop());
		assertEquals(testString,s.pop());
	}

}
