/**
 * 
 */
package main;

import java.util.Set;

import exceptions.UninitializedObjectException;

/**
 * Helper class for the linked with social network.
 * 
 * @author Shaun Howard
 */
public class LinkedWithUtilities {

	/**
	 * Throws an uninitialized object exception when this friend is invalid.
	 * 
	 * @throws UninitializedObjectException
	 *             - thrown when this friend is invalid
	 */
	static void throwExceptionWhenInvalid(boolean isValid)
			throws UninitializedObjectException {
		if (!isValid) {
			throw new UninitializedObjectException();
		}
	}

	/**
	 * Throws a null pointer exception when any of the input objects is null.
	 * 
	 * @param objs
	 *            - the objects to check for nullness
	 * @throws NullPointerException
	 *             - thrown when any of the input objects is null
	 */
	static void throwExceptionWhenNull(Object... objs)
			throws NullPointerException {
		for (Object obj : objs) {
			if (obj == null) {
				throw new NullPointerException();
			}
		}
	}

	/**
	 * Returns true when any of the input objects are null.
	 * 
	 * @param objs
	 *            - the objects to check for null
	 * @return whether any objects were null
	 */
	static boolean returnTrueWhenNull(Object... objs) {
		for (Object obj : objs) {
			if (obj == null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines whether the two input sets are the same size.
	 * Sets the social network status based on result.
	 * 
	 * @param users - the user set
	 * @param ids - the string id set
	 * @param status - the social network status to set based on this evaluation
	 */
	static void setInvalidUsersStatusIfSetSizeUnequal(Set<User> users, Set<String> ids, SocialNetworkStatus status){	
		if (users.size() != ids.size() || users.size() > 2) {
			status.setStatus(SocialNetworkStatus.Enum.INVALID_USERS);
		}
	}
	
	/**
	 * Checks if the input user id is within the given user set.
	 * Sets invalid user status when user is not in set.
	 * 
	 * @param id - the user id to look for in user set
	 * @param status - the social network status
	 * @param userSet - the user set to check for containment
	 */
	static void setStatusForInvalidUsers(String id, Set<User> userSet, SocialNetworkStatus status){
		boolean userInSet = false;

        USER_SEARCH_LOOP:
        for (User user : userSet){
            if (user.getID().equals(id)){
                userInSet = true;
                break USER_SEARCH_LOOP;
            }
        }

		if (!userInSet){
			status.setStatus(SocialNetworkStatus.Enum.INVALID_USERS);
		}
	}

    /**
     * Checks if the input distance is negative and sets the status to recognize
     * if this is the case.
     *
     * @param distance - the distance to check for negativity
     * @param status - the status of the operation
     */
    static void setStatusForNegativeDistance(int distance, SocialNetworkStatus status){
        if (distance < 0) {
            status.setStatus(SocialNetworkStatus.Enum.INVALID_DISTANCE);
        }
    }

}
