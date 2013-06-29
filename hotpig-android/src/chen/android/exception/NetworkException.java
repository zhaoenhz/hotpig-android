package chen.android.exception;

public class NetworkException extends HotpigException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NetworkException(){}
	
	public NetworkException(String message, Throwable t){
		super(message, t);
	}

}
