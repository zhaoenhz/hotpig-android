package chen.android.exception;

public class HotpigException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HotpigException(){}
	
	public HotpigException(Exception ex){
		super(ex);
	}
	
	public HotpigException(String message){
		super(message);
	}
	
	public HotpigException(String message, Throwable t){
		super(message, t);
	}
}
