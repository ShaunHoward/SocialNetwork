/**
 * 
 */
package tests;

import static org.junit.Assert.*;
import exceptions.UninitializedObjectException;
import main.Friend;
import main.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the friend class.
 * 
 * @author Shaun Howard
 */
public class TestFriend {
	
	Friend friend;

	@Before
	public void setUp() {
		friend = new Friend();
	}
	
	@Test
	public void testConstructor(){
		try {
			assertEquals(null, friend.getUser());
			assertEquals(0, friend.getDistance());
		} catch (UninitializedObjectException e) {
		}	
	}
	
	@Test
	public void testSetUser(){
		User testUser = new User();
		testUser.setID("12345");
		friend.set(testUser, 3);
		try {
			assertEquals(testUser, friend.getUser());
			assertEquals(3, friend.getDistance());
		} catch (UninitializedObjectException e) {
		}
	
	}
	
	public void testToString(){
		assertEquals("Invalid Friend", friend.toString());
		User testUser = new User();
		testUser.setID("12345");
		friend.set(testUser, 3);
		try {
			assertEquals("Friend " + testUser.getFirstName() + " " 
		+ testUser.getLastName() + " who is " + friend.getDistance() + " links away.", friend.toString());
		} catch (UninitializedObjectException e) {
		}
		
	}
}
