/**
 * 
 */
package howard.linkedwith.tests;

import static org.junit.Assert.*;
import howard.linkedwith.exceptions.UninitializedObjectException;
import howard.linkedwith.main.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the User class of the Linked With social network.
 * 
 * @author Shaun Howard
 */
public class TestUser {

	// Test user for use throughout test class.
	private User testUser, testUser2;
	
	/**
	 * Sets up the test user. 
	 */
	@Before
	public void setUp() {
		testUser = new User();
		testUser2 = new User();
		testUser2.setID("12345");
	}

	@Test
	public void testUser() {
		assertFalse("The initial test user was unexpectedly valid", testUser.isValid());
		assertEquals("The initial test user's id was unexpectedly not null.", null, testUser.getID());
	}

	@Test
	public void testSetID() {
		assertTrue("Set ID did not work as expected on new user.", testUser.setID("Shaun"));
		assertTrue("User is not valid after setting ID.", testUser.isValid());
		
		assertFalse("Set ID set user ID after ID already set.", testUser.setID("Ian"));
	}
	
	@Test (expected=NullPointerException.class)
	public void testNullSetID() {
		testUser.setID(null);
	}
	
	@Test 
	public void testSetFirstName() {
		
		try {
			testUser2.setFirstName("Shaun");
		} catch (NullPointerException | UninitializedObjectException e) {
			System.err.println("Failed to set first name of test user.");
		}
		
		assertEquals("Shaun", testUser2.getFirstName());
	}
	
	@Test (expected=NullPointerException.class)
	public void testNullSetFirstName() throws NullPointerException {
		try {
			testUser2.setFirstName(null);
		} catch (UninitializedObjectException e) {
			System.err.println("Test user was unexpectedly invalid.");
		}
	}
	
	@Test 
	public void testSetMiddleName() {
		try {
			testUser2.setMiddleName("Michael");
		} catch (NullPointerException | UninitializedObjectException e) {
			System.err.println("Failed to set middle name of test user.");
		}
		
		assertEquals("Michael", testUser2.getMiddleName());
	}
	
	@Test (expected=NullPointerException.class)
	public void testNullSetMiddleName() throws NullPointerException {
		try {
			testUser2.setMiddleName(null);
		} catch (UninitializedObjectException e) {
			System.err.println("Test user was unexpectedly invalid.");
		}
	}
	
	@Test 
	public void testSetLastName() {
		try {
			testUser2.setLastName("Howard");
		} catch (NullPointerException | UninitializedObjectException e) {
			System.err.println("Failed to set last name of test user.");
		}
		
		assertEquals("Howard", testUser2.getLastName());
	}
	
	@Test (expected=NullPointerException.class)
	public void testNullSetLastName() throws NullPointerException {
		try {
			testUser2.setLastName(null);
		} catch (UninitializedObjectException e) {
			System.err.println("Test user was unexpectedly invalid.");
		}
	}
	
	@Test 
	public void testSetEmail() {
		
		try {
			testUser2.setEmail("smh150@case.edu");
		} catch (NullPointerException | UninitializedObjectException e) {
			System.err.println("Failed to set email of test user.");
		}
		
		assertEquals("smh150@case.edu", testUser2.getEmail());
	}
	
	@Test (expected=NullPointerException.class)
	public void testNullSetEmail() throws NullPointerException {
		try {
			testUser2.setEmail(null);
		} catch (UninitializedObjectException e) {
			System.err.println("Test user was unexpectedly invalid.");
		}
	}
	
	@Test 
	public void testSetPhoneNumber() {
		
		try {
			testUser2.setPhoneNumber("570-698-5237");
		} catch (NullPointerException | UninitializedObjectException e) {
			System.err.println("Failed to set phone number of test user.");
		}
		
		assertEquals("570-698-5237", testUser2.getPhoneNumber());
	}
	
	@Test (expected=NullPointerException.class)
	public void testNullSetPhoneNumber() throws NullPointerException {
		try {
			testUser2.setPhoneNumber(null);
		} catch (UninitializedObjectException e) {
			System.err.println("Test user was unexpectedly invalid.");
		}
	}

	@Test
	public void testGetID() {
		assertEquals("Get ID did not return null when user invalid.", null, testUser.getID());
		
		testUser.setID("ShaunHoward");
		assertEquals("Get ID did not return the correct ID.", "ShaunHoward", testUser.getID());
	}

	@Test
	public void testIsValid() {
		testUser.setID("ShaunHoward");
		assertTrue("User was unexpectedly invalid after setting ID.", testUser.isValid());
	}

	@Test
	public void testToString() {
		assertEquals("User string was not denoted as uninitialized when user invalid.",
				"Invalid User: Uninitialized ID", testUser.toString());
		
		testUser.setID("ShaunHoward");
		assertEquals("User string was not denoted as ID when user is valid.",
				"ShaunHoward", testUser.toString());
	}

}
