/**
 * 
 */
package main;

import exceptions.UninitializedObjectException;

/**
 * Friend class of the social network project for EECS 293.
 * 
 * @author Shaun Howard
 */
public class Friend {

	// The user who is this friend.
	private User user;
	
	// The distance of this friend.
	private int distance;
	
	// The validity of this friend.
	private boolean isValid;
	
	/**
	 * Empty friend constructor that creates an invalid friend.
	 */
	public Friend(){
		this.user = null;
		this.distance = 0;
		this.isValid = false;
	}
	
	/**
	 * Sets a friend at the given distance and marks the friend valid.
	 * Operation is completed only on invalid friend.
	 * 
	 * @param user - the user to set as this friend
	 * @param distance - the distance to set of this friend
	 * @return whether the operation completed successfully
	 */
	public boolean set (User user, int distance) throws NullPointerException{
		LinkedWithUtilities.throwExceptionWhenNull(user, distance);
		
		if (!isValid) {
			this.user = user;
			this.distance = distance;
			this.isValid = true;
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the user of this friend. 
	 * Throws exception when the friend is invalid.
	 * 
	 * @return the user of this friend class
	 * @throws UninitializedObjectException - thrown when friend is invalid
	 */
	public User getUser() throws UninitializedObjectException{
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid);
		return this.user;
	}
	
	/**
	 * Gets the distance of this friend.
	 * 
	 * @return the distance of this friend
	 * @throws UninitializedObjectException - thrown when friend is invalid
	 */
	public int getDistance() throws UninitializedObjectException{
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid);
		return this.distance;
	}

    /**
     * Converts this friend to a readable string by the user ID and distance of
     * the friend.
     *
     * Overrides the toString() method of Object.
     *
     * @return the string representation of this friend
     */
    @Override
	public String toString() {
		if (!isValid){
			return "Invalid Friend";
		} else {
			return "Friend " + user.getID() + " who is " + distance + " links away.";
		}
	}

    /**
     * Override the equals method of Object for this Friend implementation.
     * Compares two friends by their user objects. Returns true if two user objects
     * are equivalent.
     *
     * @param object - the object to compare to this object
     */
    @Override
    public boolean equals(Object object) {
        boolean result = false;

        if (LinkedWithUtilities.returnTrueWhenNull(object)
                || object.getClass() != this.getClass()) {
            result = false;
        } else {
            Friend friend = (Friend) object;

            try {
                if (this.user.getID().equals(friend.getUser().getID())) {
                    result = true;
                }
            } catch (UninitializedObjectException e) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Override the hash code method of Object for this Friend implementation.
     * Hash code is determined from the hash code of the user object's ID and
     * from the distance of the friend.
     *
     * @return the hash code of this friend
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.user.getID() != null ? this.user.getID().hashCode() : 0);
        hash = 23 * hash + this.distance;
        return hash;
    }
}
