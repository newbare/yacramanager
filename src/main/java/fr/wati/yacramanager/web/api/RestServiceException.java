/**
 * 
 */
package fr.wati.yacramanager.web.api;

/**
 * @author Rachid Ouattara
 *
 */
@SuppressWarnings("serial")
public class RestServiceException extends Exception {

	/**
	 * 
	 */
	public RestServiceException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public RestServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RestServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public RestServiceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RestServiceException(Throwable cause) {
		super(cause);
	}

	
}
