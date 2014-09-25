/**
 * 
 */
package howard.linkedwith.main;

import java.util.Set;

import howard.linkedwith.exceptions.UninitializedObjectException;

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
		if (users.size() != ids.size()) {
			status.setStatus(SocialNetworkStatus.Enum.INVALID_USERS);
		}
	}
	
	/**
	 * Checks if the input user id is empty or is not contained in the given user set.
	 * Sets invalid user status when either is true.
	 * 
	 * @param id - the user id to check for absence
	 * @param status - the social network status
	 * @param userSet - the user set to check for containment
	 */
	static void setStatusForInvalidUsers(String id, SocialNetworkStatus status, Set<User> userSet){
		User user = new User();
		user.setID(id);
		if (id.length() == 0 || !userSet.contains(user)){
			status.setStatus(SocialNetworkStatus.Enum.INVALID_USERS);
		}
	}

}
