/**
 * 
 */
package main;

/**
 * The social network status of the social network.
 * 
 * @author Shaun Howard
 */
public class SocialNetworkStatus {
	
	/**
	 * The enumerated type for the social network status of the social network.
	 * 
	 * @author Shaun Howard
	 */
	public enum Enum {
			SUCCESS, ALREADY_VALID, INVALID_USERS, INVALID_DATE,
			ALREADY_ACTIVE, ALREADY_INACTIVE, INVALID_DISTANCE
	}
	
	// The enumerated type status of the social network.
	Enum status;
	
	/**
	 * Sets the status of the social network via enum.
	 * 
	 * @param status - the enum status of the social network to set
	 */
	public void setStatus(Enum status){
		this.status = status;
	}
	
	/**
	 * Gets the status of the social network.
	 * 
	 * @return the status of the social network as an enum
	 */
	public Enum getStatus() {
		return this.status;
	}
}
