/**
 *
 */
package tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import exceptions.UninitializedObjectException;
import main.Link;
import main.SocialNetworkStatus;
import main.User;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the Link class of the Linked With social network.
 *
 * @author Shaun Howard
 */
public class TestLink {

    // Objects to use throughout tests.
    private Link testLink;
    private SocialNetworkStatus status;
    private Set<User> testUsers;
    private User user1, user2, user3;
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private Date date1, date1_2, date2, date2_2, date3;

    /**
     * Set up objects before tests.
     *
     * @throws ParseException - thrown when date format not parsed correctly
     */
    @Before
    public void setUp() throws ParseException {
        status = new SocialNetworkStatus();
        testLink = new Link();
        testUsers = new HashSet<>();

        user1 = new User();
        user1.setID("ShaunHoward");
        testUsers.add(user1);

        user2 = new User();
        user2.setID("IanAnderson");
        testUsers.add(user2);

        user3 = new User();
        user3.setID("BillyBob");

        date1 = sdf.parse("1/1/2014");
        date1_2 = sdf.parse("1/6/2014");
        date2 = sdf.parse("2/1/2014");
        date2_2 = sdf.parse("2/27/2014");
        date3 = sdf.parse("3/1/2014");
    }

    @Test
    public void testLink() {
        assertFalse("Initial link was unexpectedly valid.", testLink.isValid());
    }

    @Test
    public void testSetUsers() {
        testLink.setUsers(testUsers, status);

        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            assertEquals("Link users were not properly assigned.",
                    testLink.getUsers(), testUsers);
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }

        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.ALREADY_VALID, status.getStatus());
    }

    @Test
    public void testThreeSetUsers() {
        testUsers.add(user3);

        testLink.setUsers(testUsers, status);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSetUsers() {
        testLink.setUsers(null, status);

        try {
            assertTrue("Link users should be empty but were not.",
                    testLink.getUsers().isEmpty());
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testIsValid() {
        assertFalse("Initial link was unexpectedly valid.", testLink.isValid());

        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        assertTrue("Link was unexpectedly invalid after users set.",
                testLink.isValid());
    }

    @Test
    public void testGetUsers() {
        try {
            assertEquals("Link users were not set expectedly.",
                    testUsers, testLink.getUsers());
        } catch (UninitializedObjectException e) {
        }
    }

    @Test
    public void testEstablish() {
        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            testLink.establish(date1, status);
            assertEquals(testLink.getDates().get(0), date1);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
        } catch (NullPointerException e) {
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }

        try {
            testLink.tearDown(date2, status);
            testLink.establish(date1, status);
            assertEquals(SocialNetworkStatus.Enum.INVALID_DATE, status.getStatus());
        } catch (NullPointerException e) {
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test(expected = UninitializedObjectException.class)
    public void testInvalidEstablish() throws NullPointerException, UninitializedObjectException {
        testLink.establish(date1, status);
    }

    @Test(expected = NullPointerException.class)
    public void testNullEstablish() throws NullPointerException {
        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            testLink.establish(null, status);
        } catch (UninitializedObjectException uoe) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testTearDown() {
        testLink.setUsers(testUsers, status);

        try {
            testLink.establish(date1, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            testLink.tearDown(date2, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            assertEquals(testLink.getDates().get(0), date1);
            assertEquals(testLink.getDates().get(1), date2);
        } catch (NullPointerException e) {
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test(expected = UninitializedObjectException.class)
    public void testInvalidTearDown() throws NullPointerException, UninitializedObjectException {
        testLink.tearDown(date1, status);
    }

    @Test(expected = NullPointerException.class)
    public void testNullTearDown() throws NullPointerException {
        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            testLink.tearDown(null, status);
        } catch (UninitializedObjectException uoe) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testIsActive() {
        testLink.setUsers(testUsers, status);

        try {
            testLink.establish(date1, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            testLink.tearDown(date2, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            testLink.establish(date3, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

            assertTrue(testLink.isActive(date1));
            assertTrue(testLink.isActive(date1_2));

            assertFalse(testLink.isActive(date2));
            assertFalse(testLink.isActive(date2_2));

            assertTrue(testLink.isActive(date3));
        } catch (NullPointerException e) {
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testFirstEvent() {
        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            testLink.establish(date1, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            assertEquals(date1, testLink.firstEvent());
        } catch (NullPointerException e) {
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testNullFirstEvent() {
        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            assertEquals(null, testLink.firstEvent());
        } catch (NullPointerException e) {
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test(expected = UninitializedObjectException.class)
    public void testInvalidFirstEvent() throws UninitializedObjectException {
        testLink.firstEvent();
    }

    @Test
    public void testNextEvent() {
        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            testLink.establish(date1, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            testLink.tearDown(date2, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());
            testLink.establish(date3, status);
            assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

            assertEquals(date2, testLink.nextEvent(date1));
            assertEquals(date2, testLink.nextEvent(date1_2));
            assertEquals(date3, testLink.nextEvent(date2));
            assertEquals(date3, testLink.nextEvent(date2_2));
        } catch (NullPointerException e) {
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test(expected = UninitializedObjectException.class)
    public void testInvalidNextEvent() throws UninitializedObjectException {
        try {
            testLink.nextEvent(date1);
        } catch (NullPointerException e) {
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullNextEvent() throws NullPointerException {
        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        try {
            testLink.nextEvent(null);
        } catch (UninitializedObjectException e) {
            fail("Unexpected exception was thrown.");
        }
    }

    @Test
    public void testToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Link between: ");

        Iterator<User> iter = testUsers.iterator();
        boolean firstUser = true;

        while (iter.hasNext()) {
            builder.append(iter.next().toString());

            if (firstUser) {
                builder.append(" and ");
                firstUser = false;
            }
        }

        testLink.setUsers(testUsers, status);
        assertEquals(SocialNetworkStatus.Enum.SUCCESS, status.getStatus());

        assertEquals(builder.toString(), testLink.toString());
    }

}
