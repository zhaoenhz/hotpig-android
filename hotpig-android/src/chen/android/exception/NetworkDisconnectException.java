package chen.android.exception;

public class NetworkDisconnectException extends NetworkException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "无法连接到Internet，请开启网络连接";
	}
}
