/**
 * 
 */
package howard.linkedwith.tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import howard.linkedwith.main.Friend;
import howard.linkedwith.main.SocialNetwork;
import howard.linkedwith.main.SocialNetworkStatus;
import howard.linkedwith.main.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the social network class of the Linked With social network.
 * 
 * @author Shaun Howard
 */
public class TestSocialNetwork {
	
	// Objects to use throughout tests.
	private SocialNetwork testNetwork;
	private SocialNetworkStatus status;
	private User user1, user2, user3;
	private Set<User> testUsersGood;
	private Set<User> testUsersBad;
	private Set<String> testUserIDs;
	private Set<String> testTwoUserIDs;
	private	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private Date date1, date2;

	/**
	 * Set up objects before tests.
	 * 
	 * @throws ParseException - thrown when date format not parsed correctly
	 */
	@Before
	public void setUp() throws ParseException {
		status = new SocialNetworkStatus();
		testUsersGood = new HashSet<>();
		testUsersBad = new HashSet<>();
		testUserIDs = new HashSet<>();
		testTwoUserIDs = new HashSet<>();
		testNetwork = new SocialNetwork();
		
		user1 = new User();
		user1.setID("ShaunHoward");
		testUsersGood.add(user1);
		testUsersBad.add(user1);
		testUserIDs.add("ShaunHoward");
		testTwoUserIDs.add("ShaunHoward");
		
		user2 = new User();
		user2.setID("IanAnderson");
		testUsersGood.add(user2);
		testUsersBad.add(user2);
		testUserIDs.add("IanAnderson");
		testTwoUserIDs.add("IanAnderson");
		
		user3 = new User();
		user3.setID("BillyBob");
		testUsersBad.add(user3);
		testUserIDs.add("BillyBob");
		
		date1 = sdf.parse("1/1/2014");
		date2 = sdf.parse("2/1/2014");
	}

	@Test
	public void testAddUser() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		testNetwork.addUser(user3);
		
		assertEquals(testNetwork.getUserSet(), testUsersBad);
	}
	
	@Test
	public void testAddDuplicateUser() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		
		assertTrue(testNetwork.getUserSet().size() == 2);
	}

	@Test
	public void testIsMember() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		testNetwork.addUser(user3);
		
		assertTrue(testNetwork.isMember(user1.getID()));
		assertTrue(testNetwork.isMember(user2.getID()));
		assertTrue(testNetwork.isMember(user3.getID()));
	}

	@Test
	public void testGetUser() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		testNetwork.addUser(user3);
		
		assertEquals(testNetwork.getUser(user1.getID()), user1);
		assertEquals(testNetwork.getUser(user2.getID()), user2);
		assertEquals(testNetwork.getUser(user3.getID()), user3);
	}
	
	@Test
	public void testNeighborhood() {
		Set<Friend> friendSet = new HashSet<>();
		
		Friend friend1 = new Friend();
		friend1.set(user1, 0);
		friendSet.add(friend1);
		Friend friend2 = new Friend();
		friend2.set(user2, 1);
		friendSet.add(friend2);
		Friend friend3 = new Friend();
		friend2.set(user3, 2);
		friendSet.add(friend3);
		
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		testNetwork.addUser(user3);
		
		Set<String> userIds1 = new HashSet<String>();
		Set<String> userIds2 = new HashSet<String>();
		userIds1.add(user1.getID());
		userIds1.add(user2.getID());
		userIds2.add(user2.getID());
		userIds2.add(user3.getID());
		
		testNetwork.establishLink(userIds1,  date1, status);
		testNetwork.establishLink(userIds2, date1, status);
		
		assertEquals(friendSet, testNetwork.neighborhood(user1.getID(), date2, status));
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
	}

	@Test
	public void testEstablishLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		
		testNetwork.establishLink(testTwoUserIDs, date1, status);
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
	}
	
	@Test
	public void testThreeEstablishLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		testNetwork.addUser(user3);

		testNetwork.establishLink(testUserIDs, date1, status);
		assertEquals(SocialNetworkStatus.Enum.INVALID_USERS, status.getStatus());
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullEstablishLink() {
		testNetwork.establishLink(null, null, status);
	}
	
	@Test
	public void testAlreadyEstablishedLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		
		testNetwork.establishLink(testTwoUserIDs, date1, status);
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
		
		testNetwork.establishLink(testTwoUserIDs, date1, status);
		assertEquals(SocialNetworkStatus.Enum.INVALID_USERS, status.getStatus());
	}

	@Test
	public void testTearDownLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		
		testNetwork.establishLink(testTwoUserIDs, date1, status);
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
		testNetwork.tearDownLink(testTwoUserIDs, date2, status);
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
		testNetwork.tearDownLink(testTwoUserIDs, date1, status);
		assertEquals(SocialNetworkStatus.Enum.INVALID_DATE, status.getStatus());
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullTearDownLink() {
		testNetwork.tearDownLink(null, null, status);
	}

	@Test
	public void testIsActive() {
		assertFalse(testNetwork.isActive(testTwoUserIDs, date2));
		
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		
		testNetwork.establishLink(testTwoUserIDs, date1, status);
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
		assertTrue(testNetwork.isActive(testTwoUserIDs, date1));
		
		testNetwork.tearDownLink(testTwoUserIDs, date2, status);
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
		assertFalse(testNetwork.isActive(testTwoUserIDs, date2));
	}

}
