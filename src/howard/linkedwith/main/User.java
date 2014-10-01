/**
 * 
 */
package main;

import exceptions.UninitializedObjectException;

/**
 * User represents a user of a social network Linked With.
 * 
 * A user is uniquely identified by an identification number.
 * 
 * @author Shaun Howard
 */
public class User {

	// Tells whether the user is valid or not.
	private boolean isValid;

	// Uniquely identifies the current user.
	private String id;

	// First, middle, and last name of the user.
	private String firstName, middleName, lastName;

	// Contact information of user.
	private String email, phone;

	/**
	 * Constructs an invalid user.
	 */
	public User() {
		this.isValid = false;
		this.id = null;
		this.firstName = null;
		this.middleName = null;
		this.lastName = null;
		this.email = null;
		this.phone = null;
	}

	/**
	 * Sets the unique ID of a Linked With user and returns true. When unique ID
	 * has already been set, returns false.
	 * 
	 * @param id
	 *            - the unique string ID to set for the user
	 * 
	 * @return true when unique ID was properly set, false if already set
	 * 
	 * @throws NullPointerException
	 *             - when the unique ID is null
	 */
	public boolean setID(String id) throws NullPointerException {

		LinkedWithUtilities.throwExceptionWhenNull(id);

		boolean idSet = false;

		// Set the id when the user is not already valid.
		if (!isValid()) {
			this.id = id;
			isValid = true;
			idSet = true;
		}

		return idSet;
	}

	/**
	 * Returns the unique identifier string corresponding to the user. When the
	 * user is invalid, returns null.
	 * 
	 * @return the unique identifier string for the user
	 */
	public String getID() {
		if (isValid()) {
			return this.id;
		}
		return null;
	}

	/**
	 * Sets the first name of this user. Will replace the first name previously
	 * stored for the user. Throws an exception if the input name is null or the
	 * user is invalid.
	 * 
	 * @param name
	 *            - the name to set
	 * 
	 * @return the current user with new name
	 * 
	 * @throws NullPointerException
	 *             - thrown when the input name is null
	 * @throws UninitializedObjectException
	 *             - thrown when the user is invalid
	 */
	public User setFirstName(String name) throws NullPointerException,
			UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());
		LinkedWithUtilities.throwExceptionWhenNull(name);

		this.firstName = name;
		return this;
	}

	/**
	 * Returns the first name of this user.
	 * 
	 * @return the first name of this user
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets the middle name of this user. Will replace the middle name
	 * previously stored for the user. Throws an exception if the input name is
	 * null or the user is invalid.
	 * 
	 * @param name
	 *            - the name to set
	 * 
	 * @return the current user with new name
	 * 
	 * @throws NullPointerException
	 *             - thrown when the input name is null
	 * @throws UninitializedObjectException
	 *             - thrown when the user is invalid
	 */
	public User setMiddleName(String name) throws NullPointerException,
			UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());
		LinkedWithUtilities.throwExceptionWhenNull(name);

		this.middleName = name;
		return this;
	}

	/**
	 * Returns the middle name of this user.
	 * 
	 * @return the middle name of this user
	 */
	public String getMiddleName() {
		return this.middleName;
	}

	/**
	 * Sets the last name of this user. Will replace the last name previously
	 * stored for the user. Throws an exception if the input name is null or the
	 * user is invalid.
	 * 
	 * @param name
	 *            - the name to set
	 * 
	 * @return the current user with new name
	 * 
	 * @throws NullPointerException
	 *             - thrown when the input name is null
	 * @throws UninitializedObjectException
	 *             - thrown when the user is invalid
	 */
	public User setLastName(String name) throws NullPointerException,
			UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());
		LinkedWithUtilities.throwExceptionWhenNull(name);

		this.lastName = name;
		return this;
	}

	/**
	 * Returns the last name of this user.
	 * 
	 * @return the last name of this user
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets the email of this user. Will replace the email previously stored for
	 * the user. Throws an exception if the input email is null or the user is
	 * invalid.
	 * 
	 * @param email
	 *            - the email to set
	 * 
	 * @return the current user with new email
	 * 
	 * @throws NullPointerException
	 *             - thrown when the input email is null
	 * @throws UninitializedObjectException
	 *             - thrown when the user is invalid
	 */
	public User setEmail(String email) throws NullPointerException,
			UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());
		LinkedWithUtilities.throwExceptionWhenNull(email);

		this.email = email;
		return this;
	}

	/**
	 * Returns the email of this user.
	 * 
	 * @return the email of this user
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the phone number of this user. Will replace the phone number
	 * previously stored for the user. Throws an exception if the input number
	 * is null or the user is invalid.
	 * 
	 * @param phone
	 *            - the phone number to set
	 * 
	 * @return the current user with new number
	 * 
	 * @throws NullPointerException
	 *             - thrown when the input number is null
	 * @throws UninitializedObjectException
	 *             - thrown when the user is invalid
	 */
	public User setPhoneNumber(String phone) throws NullPointerException,
			UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());
		LinkedWithUtilities.throwExceptionWhenNull(phone);

		this.phone = phone;
		return this;
	}

	/**
	 * Returns the phone number of this user.
	 * 
	 * @return the phone number of this user
	 */
	public String getPhoneNumber() {
		return this.phone;
	}

	/**
	 * Returns whether the user is valid or not.
	 * 
	 * @return whether the user is valid or not
	 */
	public boolean isValid() {
		return this.isValid;
	}

	/**
	 * Returns the human readable code for the user. Will tell if user is
	 * invalid.
	 * 
	 * @return a human interpretation of the user
	 */
	public String toString() {
		if (isValid()) {
			return this.id;
		}
		return "Invalid User: Uninitialized ID";
	}

	/**
	 * Override the equals method of Object for this User implementation.
	 * Compares two users by their unique IDs. Returns true if two user IDs are
	 * equivalent.
	 */
	@Override
	public boolean equals(Object object) {
		boolean result = false;

		if (LinkedWithUtilities.returnTrueWhenNull(object)
				|| object.getClass() != this.getClass()) {
			result = false;
		} else {
			User user = (User) object;

			if (this.id.equals(user.id)) {
				result = true;
			}
		}
		return result;
	}

}
