package chen.android.utils;

import java.util.ArrayList;

import chen.android.exception.FailureResponseException;


public class JSONResponse<T> {
		
	// 成功、失败标志
	private boolean success = true;
	// 表单元素错误信息
	private ArrayList<ElementError> errors = null;
	// 成功时返回的对象
	private T returnObject = null;
	//错误消息
	private String errorMessage = null;

	public boolean isSuccess() {
		if(errors != null && errors.size() > 0){
			return false;
		}
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ArrayList<ElementError> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<ElementError> errors) {
		this.errors = errors;
	}

	public void addError(ElementError error) {
		if (this.errors == null) {
			this.errors = new ArrayList<ElementError>();
		}
		this.errors.add(error);
	}

	public T getReturnObject() throws FailureResponseException {
		checkSuccess();
		return returnObject;
	}

	public JSONResponse<T> setReturnObject(T returnObject) {
		this.returnObject = returnObject;
		return this;
	}
	
	public static JSONResponse<Object> successed() {
		JSONResponse<Object> ret = new JSONResponse<Object>();
		ret.setSuccess(true);
		return ret;
	}
	
	public static <T> JSONResponse<T> successed(T obj) {
		JSONResponse<T> ret = new JSONResponse<T>();
		ret.setSuccess(true);
		ret.setReturnObject(obj);
		return ret;
	}
	
	public static <T> JSONResponse<T> errorMessage(String message){
		JSONResponse<T> ret = new JSONResponse<T>();
		ret.setSuccess(false);
		ret.setErrorMessage(message);
		return ret;
	}
	
	public static <T> JSONResponse<T> error(ElementError ... errors){
		JSONResponse<T> response = new JSONResponse<T>();
		response.setSuccess(false);
		for(ElementError error : errors){
			response.addError(error);	
		}
		return response;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void checkSuccess() throws FailureResponseException{
		if(!isSuccess()){
			throw new FailureResponseException(this);
		}
	}
	
}
