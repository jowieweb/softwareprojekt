package org.Client;

public class TCPClientException extends Throwable{
	private static final long serialVersionUID = -2409227884377279805L;
	public TCPClientException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public TCPClientException(String message)
	{
		super(message);
	}
}
