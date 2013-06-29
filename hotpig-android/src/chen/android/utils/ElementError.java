package chen.android.utils;

public class ElementError {
	// 元素名，与页面元素名一致
	private String element;
	// 错误信息
	private String message;
	
	public ElementError(String element, String message){
		setElement(element);
		setMessage(message);
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public static ElementError create(String element, String message){
		return new ElementError(element, message);
	}
}
