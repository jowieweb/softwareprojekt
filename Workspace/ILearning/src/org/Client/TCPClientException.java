package org.Client;

/**
 * Defines an exception which is thrown by TCPConnection.
 */
public class TCPClientException extends Throwable {
	private static final long serialVersionUID = -2409227884377279805L;
	
	/**
	 * Constructor awaits a message and the cause.
	 * @param message a message
	 * @param cause the problem
	 */
	public TCPClientException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor awaits a message.
	 * @param message a message
	 */
	public TCPClientException(String message) {
		super(message);
	}
}
