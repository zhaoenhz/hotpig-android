package chen.android.ui.commons;

public interface BackPressedHandler {


	/**
	 * 如果消费了事件，返回true; 否则返回false
	 * @return
	 */
	boolean handleBackPress();
}
