package chen.android.exception;

import chen.android.utils.JSONResponse;



/**
 * 当服务器端返回错误时，抛出此异常
 *
 */
public class FailureResponseException extends HotpigException {

	private static final long serialVersionUID = 1L;
	
	private JSONResponse<?> res;
	private int statusCode = -1;
	
	public FailureResponseException(int statusCode){
		this.statusCode = statusCode;
	}
	
	public FailureResponseException(JSONResponse<?> res){
		super(res.getErrorMessage());
		this.res = res;
	}
	
	public JSONResponse<?> getResponse(){
		return res;
	}
	
	public int getStatusCode(){
		return statusCode;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		String message = "发送请求出错";
		if(res != null){
			if(res.getErrorMessage() != null) {
				message = res.getErrorMessage();
			} else if(res.getErrors() != null && res.getErrors().size() >0){
				message = res.getErrors().get(0).getMessage();
			}
		}
		switch(statusCode){
		case 403 : message = "无权访问资源"; break;
		case 404 : message = "找不到请求资源"; break;
		case 500 : message = "服务器端程序出错"; break;
		}
		return message;
	}
	
	
}
