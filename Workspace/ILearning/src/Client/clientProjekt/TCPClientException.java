package clientProjekt;

public class TCPClientException extends Throwable{
	
	public TCPClientException(String message, Throwable cause)
	{
		super(message, cause);
	}
	public TCPClientException(String message)
	{
		super(message);
	}
}
