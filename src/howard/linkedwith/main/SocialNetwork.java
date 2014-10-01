/**
 * 
 */
package main;

import java.util.*;

import exceptions.UninitializedObjectException;

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

    // Table of the neighborhood trends for each unique user id.
    Hashtable<String, Map<Date, Integer>> neighborhoodTrends;

    // Set of event dates for the links in this social network.
    Set<Date> eventDates;

	/**
	 * Constructor to create a social network without any users.
	 */
	public SocialNetwork() {
		this.userSet = new HashSet<>();
		this.userLinks = new HashMap<>();
        this.neighborhoodTrends = new Hashtable<>();
        this.eventDates = new HashSet<>();
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
     * Detects the trending users of this social network based on the
     * dates of when the links in the social network changed and the
     * size of the user's neighborhood at that time.
     *
     * @param id - the id of the user to find the neighborhood trend of
     * @param status - the social network status of the operation
     * @return a map of the sizes of the input user's neighborhood
     * at given link event dates
     * @throws exceptions.UninitializedObjectException - thrown when a link
     * is uninitialized
     */
    public Map<Date, Integer> neighborhoodTrend(String id, SocialNetworkStatus status)
            throws UninitializedObjectException {
        Map<Date, Integer> neighborhoodTrend = new HashMap<>();

        LinkedWithUtilities.setStatusForInvalidUsers(id, userSet, status);

        /*
         * Find all links to user, find events of those links,
         * check neighborhood size upon each date of event,
         * add size and date to map associated with user.
         * Then get the updated neighborhood trend of this user.
         */
        if(status.getStatus() != SocialNetworkStatus.Enum.INVALID_USERS){
            updateNeighborhoodTrendForUser(id, status);
            neighborhoodTrend = neighborhoodTrends.get(id);
            status.setStatus(SocialNetworkStatus.Enum.SUCCESS);
        }

        return neighborhoodTrend;
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
     * @throws exceptions.UninitializedObjectException - thrown when a link
     * is uninitialized
	 */
	public Set<Friend> neighborhood(String id, Date date,
			SocialNetworkStatus status) throws NullPointerException, UninitializedObjectException {
		return neighborhood(id, date, userLinks.size(), status);
	}

	/**
	 * Returns all of the users actively linked through the social network at the given date
     * that are directly or indirectly associated with the user of the given id in the range of
     * the given maximum distance from the initial user.
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
     * @throws exceptions.UninitializedObjectException - thrown when a link
     * is uninitialized
	 */
	public Set<Friend> neighborhood(String id, Date date, int distance_max,
			SocialNetworkStatus status) throws NullPointerException, UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenNull(id, date, status);
		LinkedWithUtilities.setStatusForInvalidUsers(id, userSet, status);
        LinkedWithUtilities.setStatusForNegativeDistance(distance_max, status);
        Set<Friend> neighborhood = new HashSet<>();

		/*
		 * Add actively linked users to the neighborhood, including the calling user.
		 */
		if (status.getStatus() != SocialNetworkStatus.Enum.INVALID_USERS ||
                status.getStatus() != SocialNetworkStatus.Enum.INVALID_DISTANCE) {
			neighborhood = buildNeighborhood(getUser(id), date, distance_max);
		}

		return neighborhood;
	}

    /**
     * Updates the neighborhood trend for the given user id by checking
     * the neighborhood size at the dates of events of links in the
     * social network and adding them to the neighborhood trend map.
     *
     * @param id - the unique user id to update the trend for
     * @param status - the status of the neighborhood update
     */
    private void updateNeighborhoodTrendForUser(String id, SocialNetworkStatus status)
            throws UninitializedObjectException {
        Map<Date, Integer> neighborhoodTrend = getNeighborhoodTrendForUser(id);
        Set<Friend> neighborhoodAtDate;
        int neighborhoodSizeAtDate;

        /*
         * Iterate through dates of events for this social network
         * and put the neighborhood size at that date in
         * the neighborhood trend for this user.
         */
        for (Date date : eventDates){
            neighborhoodAtDate = neighborhood(id, date, status);
            neighborhoodSizeAtDate = neighborhoodAtDate.size();
            neighborhoodTrend.put(date, neighborhoodSizeAtDate);
        }
    }

    /**
     * Gets the neighborhood trend for the given user id.
     * If one does not exist for the given user, it will
     * be created and put in the neighborhood trend
     * map.
     *
     * @param id - the user to get the neighborhood trend of
     * @return the neighborhood trend map for the user
     */
    private Map<Date, Integer> getNeighborhoodTrendForUser(String id) {
        Map<Date, Integer> neighborhoodTrend;

        if (neighborhoodTrends.containsKey(id)) {
            neighborhoodTrend = neighborhoodTrends.get(id);
        }else {
            neighborhoodTrend = new HashMap<>();
            neighborhoodTrends.put(id, neighborhoodTrend);
        }

        return neighborhoodTrend;
    }

    /**
     * Gets the direct links (with distance 1) to the given user
     * which are active at the given date.
     *
     * @param user - the user to find the direct links to
     * @param date - the date to check for link activity on between this user
     *             and other users associated with this user
     * @param keySet - the key set of the map of user links for the
     *               social network
     * @return a list of direct links to the given user
     * @throws exceptions.UninitializedObjectException - thrown when a link is
     * uninitialized
     */
    private List<Link> getActiveLinksToUser(User user, Date date, Set<Set<User>> keySet)
            throws UninitializedObjectException {
        List<Link> linksToUser = new ArrayList<>();
        List<Set<User>> setsToRemove = new ArrayList<>();
        Link linkToAdd = null;

        /*
         * Gather all the links to this user by finding each set the
         * user is in and finding the link associated with that user set in
         * the user links map.
         *
         * Add the user set associated with the link to the list of sets to remove.
         */
        for (Set<User> userSet : keySet) {
            if (userSet.contains(user)) {
                linkToAdd = userLinks.get(userSet);
                addActiveLinkToList(date, linkToAdd, linksToUser);
                setsToRemove.add(userSet);
            }
        }

        /*
         * Remove all the user sets in the list of sets to remove
         * from the key set of the link map so
         * they are not processed again in the neighborhood
         * method.
         */
        for (Set<User> userSet : setsToRemove) {
            if (keySet.contains(userSet)) {
                keySet.remove(userSet);
            }
        }

        return linksToUser;
    }

    /**
     * Adds an active link to the given list of links.
     * When a link is inactive, nothing happens.
     *
     * @param date - date to check link activity on
     * @param linkToAdd - link to add to the set if active on the given
     *                  date
     * @param links - set of links to add a link to
     * @throws exceptions.UninitializedObjectException - thrown when a link is
     * uninitialized
     */
    private void addActiveLinkToList(Date date, Link linkToAdd, List<Link> links)
            throws UninitializedObjectException {
        if (linkToAdd.isActive(date)) {
            links.add(linkToAdd);
        }
    }

    /**
     * Gets the users directly linked (at a distance of 1) to the given
     * user based on the given list of direct links to the user.
     *
     * @param user - the user to find the users closest to
     * @param linksToUser - the direct links to the user
     * @return the users closest to the given user
     * @throws UninitializedObjectException - thrown when a link is
     * uninitialized
     */
    private List<User> getLinkedUsers(User user, List<Link> linksToUser) throws UninitializedObjectException {
        List<Set<User>> setsOfLinkedUsers = new ArrayList<>();
        List<User> linkedUsers = new ArrayList<>();

        /*
         * For each link in the direct links to this user, add the set
         * of users associated in the link to the list of sets of linked users.
         */
        for (Link link : linksToUser){
            setsOfLinkedUsers.add(link.getUsers());
        }

        /*
         * For each set of users associated with a link, remove this user
         * from the set and add the other user to a list of users linked to this user.
         */
        for (Set<User> userSet : setsOfLinkedUsers){
            userSet.remove(user);
            linkedUsers.add(userSet.iterator().next());
        }

        return linkedUsers;
    }

    /**
     * Builds the neighborhood of the given user to the extent of the max distance given
     * and based on the link activity on the given date.
     *
     * @param user - the user to build the neighborhood of
     * @param date - the date to build the neighborhood on,
     *             which only considers active links between users
     * @param distance_max - the max distance to build the neighborhood to
     * @return the set of friends in the neighborhood
     * @throws UninitializedObjectException - thrown when a link is uninitialized
     */
    private Set<Friend> buildNeighborhood(User user, Date date, int distance_max)
            throws UninitializedObjectException {

        // A copy of the key set of the user links map.
        Set<Set<User>> keySet = new HashSet<>();
        keySet.addAll(userLinks.keySet());

        int distance = 0;

        //Neighborhood to build.
        Set<Friend> neighborhood = new HashSet<>();

        //Set of users directly connected to the given user.
        Set<User> connectedUsers = new HashSet<>();

        connectUserWithNeighborhood(user, distance, neighborhood, connectedUsers);

        distance++;

        //Find users associated to this user and add them to the neighborhood.
        while (!connectedUsers.isEmpty()) {

            if (distance > distance_max) {
                return neighborhood;
            }

            /*
             * Add all the users linked to this user at the current distance
             * and active at the current date to the neighborhood.
             */
            addUsersToNeighborhood(date, distance, connectedUsers, neighborhood, keySet);

            distance++;
        }

        return neighborhood;
    }

    /**
     * Adds the user to the neighborhood at the given distance and
     * adds the user to the set of connected users.
     *
     * @param user - the user to connect with the neighborhood
     * @param distance - the distance of the user in the neighborhood
     * @param neighborhood - the neighborhood to add the user to
     * @param connectedUsers - the set of connected users in the neighborhood
     */
    private void connectUserWithNeighborhood(User user, int distance, Set<Friend> neighborhood,
                                             Set<User> connectedUsers) {

        //Add the user to the neighborhood at the given distance.
        addUserToNeighborhood(user, neighborhood, distance);

        //Add the given user to the set of connected users.
        connectedUsers.add(user);
    }

    /**
     * Adds all the users connected in the neighborhood at the given distance
     * to the set of friends based on the given key set of the user links map
     * for this social network. Only links active at the given date are
     * considered in the neighborhood.
     *
     * @param date - the date of the neighborhood construction, which
     *             only considers active links between users
     * @param distance - the distance of the current friend search
     * @param connectedUsers - the users connected in the neighborhood at the
     *                       given distance from the initial user
     * @param neighborhood - the friend set of the neighborhood for the initial
     *                     user
     * @param keySet - the key set of the user links map in this social network
     * @throws UninitializedObjectException - thrown when a link is uninitialized
     */
    private void addUsersToNeighborhood(Date date, int distance, Set<User> connectedUsers,
                                        Set<Friend> neighborhood, Set<Set<User>> keySet)
            throws UninitializedObjectException {

        List<Link> linksToUser;

        List<User> linkedUsers;

        //Set of connected users for iterating over.
        Set<User> tempUsers = new HashSet<>();
        tempUsers.addAll(connectedUsers);

        Iterator<User> userIter = tempUsers.iterator();

        User currUser;

        /*
         * Determine all the users connected at the given distance,
         * add them to the neighborhood, and add to the set of connected
         * users for further searching friend associations at a greater
         * distance.
         */
        while(userIter.hasNext()) {
            currUser = userIter.next();

            //Find the users linked directly to the current user.
            linksToUser = getActiveLinksToUser(currUser, date, keySet);
            linkedUsers = getLinkedUsers(currUser, linksToUser);

            /*
             * Remove the current user from the set of connected users
             * so they are not processed again.
             */
            connectedUsers.remove(currUser);

            /*
             * Connect the users associated with the current user to the
             * neighborhood of friends.
             */
            if (!linkedUsers.isEmpty()) {
                for (User linkedUser : linkedUsers) {
                    connectUserWithNeighborhood(linkedUser, distance,
                            neighborhood, connectedUsers);
                }
            }
        }

    }

	/**
	 * Adds the given user to the neighborhood as a friend at the given distance.
	 * 
	 * @param user
	 *            - user to add to the neighborhood
	 * @param neighborhood
	 *            - the neighborhood to add the user to as a friend
	 * @param distance
	 *            - the distance of this friend
	 */
	private void addUserToNeighborhood(User user, Set<Friend> neighborhood,
			int distance) {
		Friend friend = new Friend();
		friend.set(user, distance);
		neighborhood.add(friend);
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
     * @throws exceptions.UninitializedObjectException - thrown when link is uninitialized
	 */
	public void establishLink(Set<String> ids, Date date,
			SocialNetworkStatus status) throws NullPointerException, UninitializedObjectException {
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
	 *            - the status of the social network operation
	 * @param establishment
	 *            - whether establishing link
     * @throws exceptions.UninitializedObjectException - thrown when a link is uninitialized
	 */
	private void changeLink(Set<String> ids, Date date,
			SocialNetworkStatus status, boolean establishment) throws UninitializedObjectException {
		Link link = new Link();
		Set<User> users = new HashSet<User>();

		LinkedWithUtilities.throwExceptionWhenNull(ids, date, status);

		// Creates a set of users from the set of user ids.
		users = createUserSetFromIDs(ids);

		LinkedWithUtilities.setInvalidUsersStatusIfSetSizeUnequal(users, ids,
				status);

		// The link should not already be established between these users.
		if (userLinks.containsKey(users) && establishment &&
                userLinks.get(users).isActive(date)) {
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
	 *            - the link to change
	 * @param date
	 *            - date of link change
	 * @param status
	 *            - status of the operation
	 */
	private void manageLink(boolean establishment, Set<User> users, Link link,
			Date date, SocialNetworkStatus status) {

		// If users were invalid, do nothing, otherwise establish the link.
		if (establishment) {

			/*
			 * Set the users in the link to the created user set from input id
			 * set.
			 */
			link.setUsers(users, status);

			/*
			 * Attempt to establish a link at the given date between the input
			 * users.
			 */
			try {
				link.establish(date, status);
			} catch (UninitializedObjectException uoe) {
				assert false : "Unable to establish link.";
			}

			// Put users in links map when status is successful.
			putUsersInLinksMap(users, link, status);

            // Add this date to the set of event dates.
            eventDates.add(date);

		} else {
			// Check if the user link set contains the link between input users.
			if (userLinks.containsKey(users)) {

				// Attempt to tear down link between the given users.
				try {
					userLinks.get(users).tearDown(date, status);
				} catch (UninitializedObjectException uoe) {
					assert false : "Unable to tear down link.";
				}

                // Add this date to the set of event dates.
                eventDates.add(date);
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
	 *            - the status of the social network at this point
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
     * @throws exceptions.UninitializedObjectException - thrown when link is uninitialized
	 */
	public void tearDownLink(Set<String> ids, Date date,
			SocialNetworkStatus status) throws NullPointerException, UninitializedObjectException {
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

			/*
			 * Attempt to determine if the user link is active at the given
			 * date.
			 */
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
			USER_SET_LOOP:
            for (User user : userSet) {
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
