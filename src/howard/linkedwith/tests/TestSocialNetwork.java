/**
 * 
 */
package tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import exceptions.UninitializedObjectException;
import main.Friend;
import main.SocialNetwork;
import main.SocialNetworkStatus;
import main.User;

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
	private User user1, user2, user3, user4, user5;
	private Set<User> testUsersGood;
	private Set<User> testUsersBad;
	private Set<String> testUserIDs;
	private Set<String> testTwoUserIDs;
	private	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private Date date1, date2, date3;

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

        user4 = new User();
        user4.setID("JohnSmith");

        user5 = new User();
        user5.setID("TimBurton");
		
		date1 = sdf.parse("1/1/2014");
		date2 = sdf.parse("2/1/2014");
        date3 = sdf.parse("3/1/2014");
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
	public void testSmallNeighborhood() {
		Set<Friend> expectedSet = new HashSet<>();
        Set<Friend> actualSet = null;

		Friend friend1 = new Friend();
		friend1.set(user1, 0);
		expectedSet.add(friend1);

		Friend friend2 = new Friend();
		friend2.set(user2, 1);
		expectedSet.add(friend2);

		Friend friend3 = new Friend();
		friend3.set(user3, 2);
		expectedSet.add(friend3);
		
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		testNetwork.addUser(user3);
		
		Set<String> userIds1 = new HashSet<String>();
		Set<String> userIds2 = new HashSet<String>();
		userIds1.add(user1.getID());
		userIds1.add(user2.getID());
		userIds2.add(user2.getID());
		userIds2.add(user3.getID());

        try {
            testNetwork.establishLink(userIds1,  date1, status);
            testNetwork.establishLink(userIds2, date1, status);
            actualSet = testNetwork.neighborhood(user1.getID(), date2, status);
        } catch (UninitializedObjectException e) {
        }

        assertEquals(expectedSet, actualSet);
		assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
	}

    @Test
    public void testMediumNeighborhood() {
        Set<Friend> expectedSet = new HashSet<>();
        Set<Friend> actualSet = null;

        Friend friend1 = new Friend();
        friend1.set(user1, 0);
        expectedSet.add(friend1);

        Friend friend2 = new Friend();
        friend2.set(user2, 1);
        expectedSet.add(friend2);

        Friend friend3 = new Friend();
        friend3.set(user3, 2);
        expectedSet.add(friend3);

        Friend friend4 = new Friend();
        friend4.set(user4, 2);
        expectedSet.add(friend4);

        Friend friend5 = new Friend();
        friend5.set(user5, 3);
        expectedSet.add(friend5);

        testNetwork.addUser(user1);
        testNetwork.addUser(user2);
        testNetwork.addUser(user3);
        testNetwork.addUser(user4);
        testNetwork.addUser(user5);

        Set<String> userIds1 = new HashSet<String>();
        userIds1.add(user1.getID());
        userIds1.add(user2.getID());
        Set<String> userIds2 = new HashSet<String>();
        userIds2.add(user2.getID());
        userIds2.add(user3.getID());
        Set<String> userIds3 = new HashSet<String>();
        userIds3.add(user2.getID());
        userIds3.add(user4.getID());
        Set<String> userIds4 = new HashSet<String>();
        userIds4.add(user4.getID());
        userIds4.add(user5.getID());

        try {
            testNetwork.establishLink(userIds1,  date1, status);
            testNetwork.establishLink(userIds2, date1, status);
            testNetwork.establishLink(userIds3, date1, status);
            testNetwork.establishLink(userIds4, date1, status);
            actualSet = testNetwork.neighborhood(user1.getID(), date2, status);
        } catch (UninitializedObjectException e) {
        }

        assertEquals(expectedSet, actualSet);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
    }

    @Test
    public void testInactiveLinksSmallNeighborhood() {
        Set<Friend> expectedSet = new HashSet<>();
        Set<Friend> actualSet = null;

        Friend friend1 = new Friend();
        friend1.set(user1, 0);
        expectedSet.add(friend1);

        Friend friend2 = new Friend();
        friend2.set(user2, 1);

        Friend friend3 = new Friend();
        friend3.set(user3, 2);

        testNetwork.addUser(user1);
        testNetwork.addUser(user2);
        testNetwork.addUser(user3);

        Set<String> userIds1 = new HashSet<String>();
        Set<String> userIds2 = new HashSet<String>();
        userIds1.add(user1.getID());
        userIds1.add(user2.getID());
        userIds2.add(user2.getID());
        userIds2.add(user3.getID());



        try {
            testNetwork.establishLink(userIds1,  date1, status);

            testNetwork.tearDownLink(userIds1, date2, status);

            testNetwork.establishLink(userIds2, date1, status);
            actualSet = testNetwork.neighborhood(user1.getID(), date2, status);
        } catch (UninitializedObjectException e) {
        }

        assertEquals(expectedSet, actualSet);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
    }

    @Test
    public void testInactiveLinksMediumNeighborhood() {
        Set<Friend> expectedSet = new HashSet<>();
        Set<Friend> actualSet = null;

        Friend friend1 = new Friend();
        friend1.set(user1, 0);
        expectedSet.add(friend1);

        Friend friend2 = new Friend();
        friend2.set(user2, 1);
        expectedSet.add(friend2);

        Friend friend3 = new Friend();
        friend3.set(user3, 2);
        expectedSet.add(friend3);

        Friend friend4 = new Friend();
        friend4.set(user4, 2);

        Friend friend5 = new Friend();
        friend5.set(user5, 3);

        testNetwork.addUser(user1);
        testNetwork.addUser(user2);
        testNetwork.addUser(user3);
        testNetwork.addUser(user4);
        testNetwork.addUser(user5);

        Set<String> userIds1 = new HashSet<String>();
        userIds1.add(user1.getID());
        userIds1.add(user2.getID());
        Set<String> userIds2 = new HashSet<String>();
        userIds2.add(user2.getID());
        userIds2.add(user3.getID());
        Set<String> userIds3 = new HashSet<String>();
        userIds3.add(user2.getID());
        userIds3.add(user4.getID());
        Set<String> userIds4 = new HashSet<String>();
        userIds4.add(user4.getID());
        userIds4.add(user5.getID());




        try {
            testNetwork.establishLink(userIds1,  date1, status);
            testNetwork.establishLink(userIds2, date1, status);
            testNetwork.establishLink(userIds3, date1, status);

            testNetwork.tearDownLink(userIds3, date2, status);

            testNetwork.establishLink(userIds4, date1, status);
            actualSet = testNetwork.neighborhood(user1.getID(), date2, status);
        } catch (UninitializedObjectException e) {
        }

        assertEquals(expectedSet, actualSet);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
    }

    @Test
    public void testSmallNeighborhoodTrend(){

        Map<Date, Integer> expectedNeighborhoodTrend = new HashMap<>();
        Map<Date, Integer> actualNeighborhoodTrend = null;


        Friend friend1 = new Friend();
        friend1.set(user1, 0);

        Friend friend2 = new Friend();
        friend2.set(user2, 1);

        Friend friend3 = new Friend();
        friend3.set(user3, 2);

        testNetwork.addUser(user1);
        testNetwork.addUser(user2);
        testNetwork.addUser(user3);

        Set<String> userIds1 = new HashSet<String>();
        Set<String> userIds2 = new HashSet<String>();
        userIds1.add(user1.getID());
        userIds1.add(user2.getID());
        userIds2.add(user2.getID());
        userIds2.add(user3.getID());



        expectedNeighborhoodTrend.put(date1, 3);
        expectedNeighborhoodTrend.put(date2, 1);

        try {
            testNetwork.establishLink(userIds1,  date1, status);
            testNetwork.establishLink(userIds2, date1, status);
            testNetwork.tearDownLink(userIds2, date2, status);
            actualNeighborhoodTrend = testNetwork.neighborhoodTrend(user1.getID(), status);
        } catch (UninitializedObjectException e) {
        }

        assertEquals(expectedNeighborhoodTrend, actualNeighborhoodTrend);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
    }

    @Test
    public void testMediumNeighborhoodTrend() {

        Map<Date, Integer> expectedNeighborhoodTrend = new HashMap<>();
        Map<Date, Integer> actualNeighborhoodTrend = null;

        Friend friend1 = new Friend();
        friend1.set(user1, 0);

        Friend friend2 = new Friend();
        friend2.set(user2, 1);

        Friend friend3 = new Friend();
        friend3.set(user3, 2);

        Friend friend4 = new Friend();
        friend4.set(user4, 2);

        Friend friend5 = new Friend();
        friend5.set(user5, 3);

        testNetwork.addUser(user1);
        testNetwork.addUser(user2);
        testNetwork.addUser(user3);
        testNetwork.addUser(user4);
        testNetwork.addUser(user5);

        Set<String> userIds1 = new HashSet<String>();
        userIds1.add(user1.getID());
        userIds1.add(user2.getID());
        Set<String> userIds2 = new HashSet<String>();
        userIds2.add(user2.getID());
        userIds2.add(user3.getID());
        Set<String> userIds3 = new HashSet<String>();
        userIds3.add(user2.getID());
        userIds3.add(user4.getID());
        Set<String> userIds4 = new HashSet<String>();
        userIds4.add(user4.getID());
        userIds4.add(user5.getID());

        expectedNeighborhoodTrend.put(date1, 4);
        expectedNeighborhoodTrend.put(date2, 1);
        expectedNeighborhoodTrend.put(date3, 1);

        try {
            testNetwork.establishLink(userIds1,  date1, status);
            testNetwork.establishLink(userIds2, date1, status);
            testNetwork.establishLink(userIds3, date1, status);
            testNetwork.establishLink(userIds4, date1, status);

            testNetwork.tearDownLink(userIds2, date2, status);

            testNetwork.establishLink(userIds2, date3, status);
            testNetwork.tearDownLink(userIds3, date3, status);
            actualNeighborhoodTrend = testNetwork.neighborhoodTrend(user1.getID(), status);
        } catch (UninitializedObjectException e) {
        }

        assertEquals(expectedNeighborhoodTrend, actualNeighborhoodTrend);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
    }

	@Test
	public void testEstablishLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);

        try {
            testNetwork.establishLink(testTwoUserIDs, date1, status);
        } catch (UninitializedObjectException e) {
        }
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
	}
	
	@Test
	public void testThreeEstablishLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);
		testNetwork.addUser(user3);

        try {
            testNetwork.establishLink(testUserIDs, date1, status);
        } catch (UninitializedObjectException e) {
        }
        assertEquals(SocialNetworkStatus.Enum.INVALID_USERS, status.getStatus());
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullEstablishLink() {
        try {
            testNetwork.establishLink(null, null, status);
        } catch (UninitializedObjectException e) {
        }
    }
	
	@Test
	public void testAlreadyEstablishedLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);

        try {
            testNetwork.establishLink(testTwoUserIDs, date1, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

            testNetwork.establishLink(testTwoUserIDs, date1, status);
            assertEquals(SocialNetworkStatus.Enum.INVALID_USERS, status.getStatus());
        } catch (UninitializedObjectException e) {
        }
	}

	@Test
	public void testTearDownLink() {
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);

        try {
            testNetwork.establishLink(testTwoUserIDs, date1, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            testNetwork.tearDownLink(testTwoUserIDs, date2, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            testNetwork.tearDownLink(testTwoUserIDs, date1, status);
            assertEquals(SocialNetworkStatus.Enum.INVALID_DATE, status.getStatus());
        } catch (UninitializedObjectException e) {
        }
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullTearDownLink() {
        try {
            testNetwork.tearDownLink(null, null, status);
        } catch (UninitializedObjectException e) {
        }
    }

	@Test
	public void testIsActive() {
		assertFalse(testNetwork.isActive(testTwoUserIDs, date2));
		
		testNetwork.addUser(user1);
		testNetwork.addUser(user2);

        try {
            testNetwork.establishLink(testTwoUserIDs, date1, status);
        } catch (UninitializedObjectException e) {
        }
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
		assertTrue(testNetwork.isActive(testTwoUserIDs, date1));

        try {
            testNetwork.tearDownLink(testTwoUserIDs, date2, status);
        } catch (UninitializedObjectException e) {
        }
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
		assertFalse(testNetwork.isActive(testTwoUserIDs, date2));
	}

}
