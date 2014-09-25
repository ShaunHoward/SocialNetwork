/**
 * 
 */
package howard.linkedwith.main;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import howard.linkedwith.exceptions.UninitializedObjectException;
import howard.linkedwith.main.User;

/**
 * Social Network represents the collection of all users in the Linked With
 * social network and their connections.
 * 
 * @author Shaun Howard
 */
public class SocialNetwork {

	// Set of users for the social network.
	Set<User> userSet;

	// Map of links between sets of users in the social network.
	Map<Set<User>, Link> userLinks;

	/**
	 * Constructor to create a social network without any users.
	 */
	public SocialNetwork() {
		this.userSet = new HashSet<User>();
		this.userLinks = new HashMap<Set<User>, Link>();
	}

	/**
	 * Adds the given user to the user set and returns true if user added
	 * correctly. Returns false if the social network already contains a user of
	 * this name.
	 * 
	 * @param user
	 *            - the user to add to the social network
	 * 
	 * @return true if the user added correctly or false if the user already
	 *         existed in the network
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 */
	public boolean addUser(User user) throws NullPointerException {

		LinkedWithUtilities.throwExceptionWhenNull(user);

		// Check if the user is valid and exists and
		// if the set of users does not contain the input user.
		if (user.isValid() && !userSet.contains(user)) {
			userSet.add(user);
			return true;
		}

		// Otherwise return false because the user set contains this user.
		return false;
	}

	/**
	 * Gets the set of users for this social network.
	 * 
	 * @return the set of users for this social network
	 */
	public Set<User> getUserSet() {
		return this.userSet;
	}

	/**
	 * Checks if the unique user id is a member of the social network.
	 * 
	 * @param id
	 *            - the unique id to check as a user in the network
	 *
	 * @return true if the id is a member of the network, false otherwise
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 */
	public boolean isMember(String id) throws NullPointerException {
		Iterator<User> iter = userSet.iterator();

		LinkedWithUtilities.throwExceptionWhenNull(id);

		// Check the set of users for the given user id.
		while (iter.hasNext()) {
			if (iter.next().toString().equals(id)) {
				return true;
			}
		}

		// Otherwise user is not a member of this social network.
		return false;
	}

	/**
	 * Gets the user with the given unique id or null if user does not exist in
	 * the network.
	 * 
	 * @param id
	 *            - the unique id to find in the social network
	 * 
	 * @return the user with the given unique id or null when the user does not
	 *         exist in network
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 */
	public User getUser(String id) throws NullPointerException {
		Iterator<User> iter = userSet.iterator();
		User user;

		LinkedWithUtilities.throwExceptionWhenNull(id);

		// Find the input user's id and return the user if exists.
		while (iter.hasNext()) {
			user = iter.next();

			if (user.toString().equals(id)) {
				return user;
			}
		}

		// Otherwise, user does not exist in system.
		return null;
	}

	/**
	 * Returns all of the users through the social network that are directly or
	 * indirectly associated with the user of the given id as well as the
	 * distance from the initial user.
	 * 
	 * @param id
	 *            - the user to get the friends of
	 * @param date
	 *            - the date to get the friends of this user
	 * @param status
	 *            - the social network status of the operation
	 * @return the set of users that are friends with this user
	 * @throws NullPointerException
	 *             - thrown when arguments are null
	 */
	public Set<Friend> neighborhood(String id, Date date,
			SocialNetworkStatus status) throws NullPointerException {
		return neighborhood(id, date, userLinks.size(), status);
	}

	/**
	 * Returns all of the users through the social network that are directly or
	 * indirectly associated with the user of the given id as well as the
	 * distance from the initial user.
	 * 
	 * @param id
	 *            - the user to get the friends of
	 * @param date
	 *            - the date to get the friends of this user
	 * @param distance_max
	 *            - the maximum distance of friends to get
	 * @param status
	 *            - the social network status of the operation
	 * @return the set of users that are friends with this user
	 * @throws NullPointerException
	 *             - thrown when arguments are null
	 */
	public Set<Friend> neighborhood(String id, Date date, int distance_max,
			SocialNetworkStatus status) throws NullPointerException {
		LinkedWithUtilities.throwExceptionWhenNull(id, date, status);
		LinkedWithUtilities.setStatusForInvalidUsers(id, status, userSet);

		Set<Set<User>> userKeySet = new HashSet<Set<User>>();
		userKeySet.addAll(userLinks.keySet());
		Set<Friend> friendSet = new HashSet<Friend>();

		// Add users to friend set, including the calling user.
		if (status.getStatus() != SocialNetworkStatus.Enum.INVALID_USERS) {
			int distance = 0;
			User user = getUser(id);
			addUserToFriendSet(user, friendSet, distance);
			distance++;
			friendSet = addFriendsToFriendSet(userKeySet, friendSet, user,
					distance_max, status);
		}
		return friendSet;
	}

	/**
	 * Adds the friends of this user to the friend set.
	 * 
	 * @param userKeySet
	 *            - the set of users linked in this social network
	 * @param friendSet
	 *            - the set of friends of this user
	 * @param user
	 *            - the user
	 * @param distance_max
	 *            - the max distance of friends
	 * @param status
	 *            - the status of this operation
	 * @return the friend set of this user
	 */
	private Set<Friend> addFriendsToFriendSet(Set<Set<User>> userKeySet,
			Set<Friend> friendSet, User user, int distance_max,
			SocialNetworkStatus status) {

		Iterator<Set<User>> userLinksIter = userLinks.keySet().iterator();
		Set<User> iter;
		int distance = 1;
		// Iterate through friends while there are more friends connected to the
		// input user's friends.
		while (!userKeySet.isEmpty() && distance <= distance_max) {
			iter = userLinksIter.next();
			// Find all friends connected to this friend and add to friends set.
			while (userLinksIter.hasNext()) {

				// When this set contains a friend of the user, get the other
				// friend and add them to
				// the friend set.
				if (iter.contains(user)) {
					iter.remove(user);
					user = iter.iterator().next();
					addUserToFriendSet(user, friendSet, distance);
					userKeySet.remove(userLinksIter);
				}

				userLinksIter.remove();
			}
			distance++;
		}
		status.setStatus(SocialNetworkStatus.Enum.SUCCESS);

		return friendSet;
	}

	/**
	 * Adds the given user to the friend set at the given distance.
	 * 
	 * @param user
	 *            - user to add to friend set
	 * @param friendSet
	 *            - the friend set to add user to
	 * @param distance
	 *            - the distance of this friend
	 */
	private void addUserToFriendSet(User user, Set<Friend> friendSet,
			int distance) {
		Friend friend = new Friend();
		friend.set(user, distance);
		friendSet.add(friend);
	}

	/**
	 * Establish a link between the two user ids on the given date if the users
	 * are valid and are not linked in the system already then return true.
	 * 
	 * When the users are invalid, already linked, or the date precedes the last
	 * date on record, does nothing but return false.
	 * 
	 * @param ids
	 *            - the unique ids to establish a link between
	 * @param date
	 *            - the date to establish the link between two users
	 * @param status
	 *            - the status of the social network
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 */
	public void establishLink(Set<String> ids, Date date,
			SocialNetworkStatus status) throws NullPointerException {
		changeLink(ids, date, status, true);
	}

	/**
	 * Changes the link based on input.
	 * 
	 * @param ids
	 *            - user ids to link
	 * @param date
	 *            - date of the change
	 * @param status
	 *            - status of the operation
	 * @param establishment
	 *            - whether establishing link
	 */
	private void changeLink(Set<String> ids, Date date,
			SocialNetworkStatus status, boolean establishment) {
		Link link = new Link();
		Set<User> users = new HashSet<User>();

		LinkedWithUtilities.throwExceptionWhenNull(ids, date, status);

		// Creates a set of users from the set of user ids.
		users = createUserSetFromIDs(ids);

		LinkedWithUtilities.setInvalidUsersStatusIfSetSizeUnequal(users, ids,
				status);

		// The link should not already be established between these users.
		if (userLinks.containsKey(users) && establishment) {
			status.setStatus(SocialNetworkStatus.Enum.INVALID_USERS);
		}

		if (SocialNetworkStatus.Enum.INVALID_USERS != status.getStatus()) {
			manageLink(establishment, users, link, date, status);
		}

	}

	/**
	 * Manages the given link change.
	 * 
	 * @param establishment
	 *            - whether to establish the link
	 * @param users
	 *            - the users in the link
	 * @param link
	 *            - the link
	 * @param date
	 *            - date of link change
	 * @param status
	 *            - status of the operation
	 */
	private void manageLink(boolean establishment, Set<User> users, Link link,
			Date date, SocialNetworkStatus status) {

		// If users were invalid, do nothing, otherwise establish the link.
		if (establishment) {

			// Set the users in the link to the created user set from input id
			// set.
			link.setUsers(users, status);

			// Attempt to establish a link at the given date between the input
			// users.
			try {
				link.establish(date, status);
			} catch (UninitializedObjectException uoe) {
				assert false : "Unable to establish link.";
			}

			// Put users in links map when status is successful.
			putUsersInLinksMap(users, link, status);
		} else {
			// Check if the user link set contains the link between input users.
			if (userLinks.containsKey(users)) {

				// Attempt to tear down link between the given users.
				try {
					userLinks.get(users).tearDown(date, status);
				} catch (UninitializedObjectException uoe) {
					assert false : "Unable to tear down link.";
				}
			} else {
				status.setStatus(SocialNetworkStatus.Enum.INVALID_USERS);
			}
		}

	}

	/**
	 * Puts the users in the links map with the key as user and the link as
	 * value.
	 * 
	 * @param users
	 *            - the users to put in the map
	 * @param link
	 *            - the link between the users
	 * @param status
	 *            - the status of the social network until now
	 */
	private void putUsersInLinksMap(Set<User> users, Link link,
			SocialNetworkStatus status) {
		// When successful, put the user links in the link map.
		if (SocialNetworkStatus.Enum.SUCCESS.equals(status.getStatus())) {
			// Put set of users and link into user links map.
			userLinks.put(users, link);
		}
	}

	/**
	 * Tear down the link between the two given user ids at the given date and
	 * return true if operation succeeded.
	 * 
	 * When the users are invalid, already unlinked, or the date precedes the
	 * last date on record, does nothing but return false.
	 * 
	 * @param ids
	 *            - the unique ids to tear down the link between
	 * @param date
	 *            - the date to tear down the link between two users
	 * @param status
	 *            - the status of the social network
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 */
	public void tearDownLink(Set<String> ids, Date date,
			SocialNetworkStatus status) throws NullPointerException {
		changeLink(ids, date, status, false);
	}

	/**
	 * Returns whether a link exists between two unique user ids and is active
	 * at the given date.
	 * 
	 * @param ids
	 *            - the unique ids to check the link activity between
	 * @param date
	 *            - the date to check for link activity on between two users
	 * 
	 * @return whether a link exists between two user ids and if the link is
	 *         valid at the given date
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 */
	public boolean isActive(Set<String> ids, Date date)
			throws NullPointerException {
		Set<User> users;

		LinkedWithUtilities.throwExceptionWhenNull(ids, date);

		// Creates a set of users from the set of user ids.
		users = createUserSetFromIDs(ids);

		/*
		 * Given the nature of hash sets to eliminate duplicates, if user ids
		 * were the same, the set would have eliminated the duplicate upon
		 * adding so the size of the resulting set would not be the same as that
		 * of the input id set.
		 */
		if (users.size() != ids.size()) {
			return false;
		}

		// Check if the map of user links contains a link between these users.
		if (userLinks.containsKey(users)) {

			// Attempt to determine if the user link is active at the given
			// date.
			try {
				return userLinks.get(users).isActive(date);
			} catch (UninitializedObjectException uoe) {
				// Let flow to return false at end.
				System.err.println("Unable to determine status of user link.");
			}
		}

		return false;
	}

	/**
	 * Creates a set of users from the input set of user ids.
	 * 
	 * @param ids
	 *            - the set of user ids to create a user set from
	 * @return a set of users representing the user ids input
	 */
	private Set<User> createUserSetFromIDs(Set<String> ids) {

		// Create a set of users.
		Set<User> users = new HashSet<>();

		// Iterate through set of user IDs.
		for (String id : ids) {
			// Check for each user id in the user set of this social network.
			USER_SET_LOOP: for (User user : userSet) {
				// When user's id is the same as id in the input set, add user
				// to new user set.
				if (user.getID().equals(id)) {
					users.add(user);
					break USER_SET_LOOP;
				}
			}
		}

		return users;
	}
}