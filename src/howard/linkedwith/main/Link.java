/**
 * 
 */
package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import exceptions.UninitializedObjectException;

/**
 * Link represents a link of two users in a social network Linked With.
 * 
 * A link tracks the dates of events between two users and whether they are
 * still linked actively.
 * 
 * @author Shaun Howard
 */
public class Link {

	// Tracks if the link is yet established.
	private boolean isValid;

	// The set of users paired in the link.
	private Set<User> users;

	// The dates of events between paired users.
	private List<Date> dates;

	/**
	 * Empty constructor creates a new link that is not yet established.
	 */
	public Link() {
		this.isValid = false;
		this.dates = new ArrayList<>();
	}

	/**
	 * Sets a valid link between two users and returns true. When link already
	 * exists or users are invalid, does nothing but return false.
	 * 
	 * @param users
	 *            - the two valid users to link
	 * @param status
	 *            - the status of the operation
	 * 
	 * @throws NullPointerException
	 *             - thrown when any arguments are null
	 */
	public void setUsers(Set<User> users, SocialNetworkStatus status)
			throws NullPointerException {

		LinkedWithUtilities.throwExceptionWhenNull(users, status);

		// Check if link is valid and there are strictly two users.
		if (!isValid() && users.size() == 2) {
			this.users = users;
			this.isValid = true;
			status.setStatus(SocialNetworkStatus.Enum.SUCCESS);
		} else if (isValid()) {
			status.setStatus(SocialNetworkStatus.Enum.ALREADY_VALID);
		} else {
			status.setStatus(SocialNetworkStatus.Enum.INVALID_USERS);
		}
	}

	/**
	 * Returns whether the link is valid between two users.
	 * 
	 * @return whether the link is valid between two users
	 */
	public boolean isValid() {
		return this.isValid;
	}

	/**
	 * Gets the two users linked with the link.
	 * 
	 * @return the two users linked with the link
	 * 
	 * @throws UninitializedObjectException
	 *             - thrown when link is invalid
	 */
	public Set<User> getUsers() throws UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());

		return this.users;
	}

	/**
	 * Gets the list of dates for the link.
	 * 
	 * @return the list of dates for the link.
	 */
	public List<Date> getDates() {
		return dates;
	}

	/**
	 * Establishes the link at the given date and returns true if successfully
	 * completed. When the link is already active or if the given date precedes
	 * the last date on record, the link remains unchanged and false is
	 * returned.
	 * 
	 * @param date
	 *            - the date to establish the link
	 * @param status
	 *            - the status of the operation
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 * @throws UninitializedObjectException
	 *             - thrown when the link is invalid
	 */
	public void establish(Date date, SocialNetworkStatus status)
			throws NullPointerException, UninitializedObjectException {
		changeLinkOnDate(date, true, status);
	}

	/**
	 * Determines if the given date is an acceptable establishment date.
	 * 
	 * @param date
	 *            - the date to check for acceptance as establishment date
	 * 
	 * @return whether the date is acceptable for establishment
	 */
	private boolean isAcceptableEstablishmentDate(Date date) {

		// Check if no other dates exist and date is not null.
		if (dates.isEmpty() && !LinkedWithUtilities.returnTrueWhenNull(date)) {
			return true;
		} else if (LinkedWithUtilities.returnTrueWhenNull(date)) {
			// Return false if null date.
			return false;
		}

		/*
		 * Check whether the last date in the list of dates was a tear down date
		 * and that the input date does not precede the last date in the list.
		 */
		return checkTearDownAndSucceedingDate(date);
	}

	/**
	 * Checks whether the last date in the list of dates was a tear down date
	 * and that the input date does not precede the last date in the list.
	 * 
	 * @param date
	 *            - the date to check
	 * 
	 * @return whether the last date in the list was a tear down date and that
	 *         the input date does not precede the last date in the list
	 */
	private boolean checkTearDownAndSucceedingDate(Date date) {
		return dates.size() % 2 == 0
				&& !date.before(dates.get(dates.size() - 1));
	}

	/**
	 * Tears down the link at the given date and returns true if successfully
	 * completed. When the link is already inactive or if the given date
	 * precedes the last date on record, the link remains unchanged and false is
	 * returned.
	 * 
	 * @param date
	 *            - the date to tear down the link
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 * @throws UninitializedObjectException
	 *             - thrown when the link is invalid
	 */
	public void tearDown(Date date, SocialNetworkStatus status)
			throws NullPointerException, UninitializedObjectException {
		changeLinkOnDate(date, false, status);
	}

	/**
	 * Checks if the input date precedes the last date on record. If so, the
	 * social network status is set to INVALID_DATE.
	 * 
	 * @param date
	 *            - the date to compare with the last date on record
	 * @param status
	 *            - the status of the social network
	 */
	private void setInvalidDateStatus(Date date,
                                      SocialNetworkStatus status) {
		if (dates.size() > 1 && date.before(dates.get(dates.size() - 1))) {
			status.setStatus(SocialNetworkStatus.Enum.INVALID_DATE);
		}
	}

	/**
	 * Determines if the given date is an acceptable tear down date.
	 * 
	 * @param date
	 *            - the date to check for acceptance as tear down date
	 * 
	 * @return whether the date is acceptable for tear down
	 */
	private boolean isAcceptableTearDownDate(Date date) {

		// Check if there is no establishment date or if input date is null.
		if (dates.isEmpty() || LinkedWithUtilities.returnTrueWhenNull(date)) {
			return false;
		}

		/*
		 * Check whether the last date in the list of dates was an establishment
		 * date and that the input date does not precede the last date in the
		 * list.
		 */
		return dates.size() % 2 != 0
				&& !date.before(dates.get(dates.size() - 1));
	}

	/**
	 * Returns whether the link is active at the given date. When there are
	 * multiple events on the same date, the last event on the date will
	 * determine the if the link is active or not.
	 * 
	 * @param date
	 *            - the date to check for link activity on
	 * 
	 * @return whether the link is active at the given date
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 * @throws UninitializedObjectException
	 *             - thrown when the link is invalid
	 */
	public boolean isActive(Date date) throws NullPointerException,
			UninitializedObjectException {		
		LinkedWithUtilities.throwExceptionWhenNull(date);
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());
		
		// Check if the link is/was active on the input date.
		if (dates.size() > 0 && isAcceptableActiveDate(date)) {
			return true;
		} 
	
		return false;
	}

	/**
	 * Determines if the given date is an acceptable active link date.
	 * 
	 * @param date
	 *            - the date to check for acceptance as active date
	 * 
	 * @return whether the link is/was active on the input date
	 */
	private boolean isAcceptableActiveDate(Date date) {
		if (LinkedWithUtilities.returnTrueWhenNull(date)) {
			return false;
		}

		Iterator<Date> iter = dates.listIterator();
		Date dateInList = null;

		// Check if the input date is before a tear down date or
		// it is equal to an establishment date.
		while (iter.hasNext()) {
			dateInList = iter.next();
			if (isActiveInDateRange(date, dateInList)) {
				return true;
			}
		}

		// Otherwise check if date is active after last date in list.
		return isActiveAfterLastDate(date, dateInList);
	}

	/**
	 * Returns whether the input date is before a tear down date or is equal to
	 * an establishment date.
	 * 
	 * @param dateToCheck
	 *            - the date to check for activity
	 * @param dateInList
	 *            - the date to compare dateToCheck with
	 * 
	 * @return whether the date to check is before a tear down date in list or
	 *         is equal to an establishment date in list
	 */
	private boolean isActiveInDateRange(Date dateToCheck, Date dateInList) {

		/*
		 * Check if date to check precedes date in list and that date is a tear
		 * down date. Otherwise, the date may be equal to an establishment date.
		 */
		if ((dateToCheck.before(dateInList)
				&& isTearDownDate(dateInList))) {
			return true;
		}
		else if (isEstablishmentDateEqualToDateInList(dateToCheck, dateInList)) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if the date to check is equal to the date in list and if the date
	 * in list is an establishment date.
	 * 
	 * @param dateToCheck
	 *            - date to compare with date in list
	 * @param dateInList
	 *            - date to check for establishment
	 * 
	 * @return whether the date to check is equal to the date in the list and if
	 *         that date is an establishment date
	 */
	private boolean isEstablishmentDateEqualToDateInList(Date dateToCheck,
                                                         Date dateInList) {
		return dateToCheck.equals(dateInList)
				&& isEstablishmentDate(dateInList);
	}

	/**
	 * Returns whether the date to check is active after the last date in list.
	 * 
	 * @param dateToCheck
	 *            - the date to check for activity
	 * @param lastDate
	 *            - the last date in date list
	 * 
	 * @return whether the date to check is active after the last date in the
	 *         list
	 */
	private boolean isActiveAfterLastDate(Date dateToCheck, Date lastDate) {
		return dateToCheck.after(lastDate) && isEstablishmentDate(lastDate);
	}

	/**
	 * Returns if the input date is an establishment date.
	 * 
	 * @param dateInList
	 *            - the date to check for establishment on
	 * 
	 * @return whether the input date is an establishment date
	 */
	private boolean isEstablishmentDate(Date dateInList) {
		// Check if there are no dates or if input date is null.
		if (dates.isEmpty()
				|| LinkedWithUtilities.returnTrueWhenNull(dateInList)) {
			return false;
		}

		// Return whether the date is an establishment date.
		return dates.indexOf(dateInList) % 2 == 0
				|| dates.indexOf(dateInList) == 0;
	}

	/**
	 * Returns if the input date is a tear down date.
	 * 
	 * @param dateInList
	 *            - the date to check for tear down on
	 * 
	 * @return whether the input date is an tear down date
	 */
	private boolean isTearDownDate(Date dateInList) {
		// Check if there are no dates or if input date is null.
		if (dates.isEmpty()
				|| LinkedWithUtilities.returnTrueWhenNull(dateInList)) {
			return false;
		}

		// Return whether the date is a tear down date.
		return dates.indexOf(dateInList) > -1
				&& dates.indexOf(dateInList) % 2 != 0;
	}

	/**
	 * Returns the date of the first event recorded in the link. When no event
	 * has yet been established, returns null.
	 * 
	 * @return the date of the first event in the link or null if no event has
	 *         happened
	 * 
	 * @throws UninitializedObjectException
	 *             - thrown when the link is invalid
	 */
	public Date firstEvent() throws UninitializedObjectException {
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());

		// Check if list of dates contains dates and the link is valid.
		if (!dates.isEmpty()) {
			return dates.get(0);
		}
		return null;
	}

	/**
	 * Returns the date of the next event after the date given. If no such event
	 * exists, returns null.
	 * 
	 * @param date
	 *            - the date before the next event
	 * 
	 * @return the date of the next event after the given date or null when no
	 *         event exists
	 * 
	 * @throws NullPointerException
	 *             - thrown when input is null
	 * @throws UninitializedObjectException
	 *             - thrown when the link is invalid
	 */
	public Date nextEvent(Date date) throws NullPointerException,
			UninitializedObjectException {

		LinkedWithUtilities.throwExceptionWhenNull(date);
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());

		return getDateAfter(date);
	}

	/**
	 * Gets the date after the input date from the list of dates.
	 * 
	 * @param date
	 *            - the date for the return date to come after
	 * 
	 * @return the date after the input date from the list of dates
	 */
	private Date getDateAfter(Date date) {
		Iterator<Date> iter = dates.listIterator();
		Date dateInList = null;

		// Check for the date that comes after the input date.
		while (iter.hasNext()) {
			dateInList = iter.next();
			if (date.before(dateInList)) {
				return dateInList;
			}
		}

		// Otherwise there is no following date in the list.
		return null;
	}

	/**
	 * Sets the link activity status based on if establishment or tear down.
	 *
     * @param isActive - whether the link is currently active
	 * @param status
	 *            - the status to change based on activity
	 * @param establishment - if link is trying to be established
	 * @throws NullPointerException
	 *             - thrown when the entered date is null
	 * @throws UninitializedObjectException
	 *             - thrown when the link is invalid on the given date
	 */
	private void setLinkActivityStatus(boolean isActive,
			SocialNetworkStatus status, boolean establishment) throws NullPointerException,
			UninitializedObjectException {
		if (isActive && establishment) {
			status.setStatus(SocialNetworkStatus.Enum.ALREADY_ACTIVE);
		} else if (!(isActive || establishment)){
		    status.setStatus(SocialNetworkStatus.Enum.ALREADY_INACTIVE);
		}
	}

	/**
	 * Changes the link on the specified date based on input parameters.
	 * 
	 * @param date
	 *            - date to change link on
	 * @param establishment
	 *            - if the link is being established
	 * @param status
	 *            - the status of the operation
	 * @throws NullPointerException
	 *             - thrown when input null
	 * @throws UninitializedObjectException
	 *             - thrown when link invalid
	 */
	private void changeLinkOnDate(Date date, boolean establishment, SocialNetworkStatus status)
            throws NullPointerException, UninitializedObjectException {

		if (linkChangeIsValid(date, establishment, status)) {
			dates.add(date);
			status.setStatus(SocialNetworkStatus.Enum.SUCCESS);
		}
	}

	/**
	 * Checks if the link change is valid.
	 * 
	 * @param date
	 *            - date to change link at
	 * @param establishment
	 *            - if the link is being established
	 * @param status
	 *            - the status of the operation
	 * @throws NullPointerException
	 *             - thrown when input null
	 * @throws UninitializedObjectException
	 *             - thrown when link invalid
	 */
	private boolean linkChangeIsValid(Date date, boolean establishment, SocialNetworkStatus status)
            throws NullPointerException, UninitializedObjectException {
		boolean linkChangeIsValid = false;

		LinkedWithUtilities.throwExceptionWhenNull(date, status);
		LinkedWithUtilities.throwExceptionWhenInvalid(isValid());
		setInvalidDateStatus(date, status);

		if (status.getStatus() != SocialNetworkStatus.Enum.INVALID_DATE) {
			if (!linkAlreadyInState(date, establishment, status)) {
				linkChangeIsValid = verifyAcceptableLinkChange(date, establishment);
			}
		}

		return linkChangeIsValid;
	}

	/**
	 * Verifies if the link change is acceptable for the type of change
     * and the date to change on. Returns true if operation completed
	 * successfully.
	 * 
	 * @param date
	 *            - the date to check for acceptable change
	 * @param establishment - whether the link is trying to establish
	 * @return whether the link can be changed based on current state
	 */
	private boolean verifyAcceptableLinkChange(Date date, boolean establishment) {

		if (establishment && isAcceptableEstablishmentDate(date)) {
			return true;
		}
		if (!establishment && isAcceptableTearDownDate(date)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the link is already in established or tear down state.
	 * 
	 * @param date
	 *            - the date to check the link at
	 * @param establishment
	 *            - whether the link is trying to establish
	 * @param status
	 *            - the status of this operation
	 * @return if the link is already in the desired state
	 * @throws NullPointerException
	 *             - thrown when input is null
	 * @throws UninitializedObjectException
	 *             - thrown when link is invalid
	 */
	private boolean linkAlreadyInState(Date date, boolean establishment, SocialNetworkStatus status)
            throws NullPointerException, UninitializedObjectException {
		boolean linkAlreadyInState = false;
		boolean isActive = isActive(date);

		// Check link in given state already.
		if ((establishment && isActive)
				|| (!establishment && !isActive)) {
			linkAlreadyInState = true;
			setLinkActivityStatus(isActive, status, establishment);
		}
		return linkAlreadyInState;
	}

	/**
	 * Returns the human readable code for the link. Will tell if link is
	 * invalid.
	 * 
	 * @return a human interpretation of the link
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Link between: ");

		Iterator<User> iter = users.iterator();
		boolean firstUser = true;

		// Append users to string builder with 'and' separating them.
		while (iter.hasNext()) {
			builder.append(iter.next().toString());

			// When user id is first user, append 'and' after first user.
			if (firstUser) {
				builder.append(" and ");
				firstUser = false;
			}
		}

		return builder.toString();
	}

}
