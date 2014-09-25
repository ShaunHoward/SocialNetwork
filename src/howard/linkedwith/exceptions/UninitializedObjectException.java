/**
 * 
 */
package howard.linkedwith.exceptions;

/**
 * An UninitializedObjectException represents an exception caused in 
 * the representation of the Linked With social network.
 * 
 * Usual reasons for throwing this exception deal with referencing objects
 * which do not exist in the social network.
 * 
 * @author Shaun Howard
 */
public class UninitializedObjectException extends Exception {

	/**
	 * Serial version UID for uniqueness as an exception.
	 */
	private static final long serialVersionUID = 401968619655424446L;

	/**
	 * Creates a generic UninitializedObjectException without parameters.
	 */
	public UninitializedObjectException() {
	}

	/**
	 * Creates an UninitializedObjectException with a string message.
	 * 
	 * @param message - the message for detailing the exception
	 */
	public UninitializedObjectException(String message) {
		super(message);
	}

	/**
	 * Creates an UninitializedObjectException with a cause that is 
	 * throwable.
	 * 
	 * @param cause - the throwable cause for this exception
	 */
	UninitializedObjectException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates an UninitializedObjectException with a string message and
	 * cause that is throwable.
	 * 
	 * @param message - the message for detailing the exception
	 * @param cause - the throwable cause for this exception
	 */
	public UninitializedObjectException(String message, Throwable cause) {
		super(message, cause);
	}

}
