package chen.android.core;


import android.app.Activity;

public class Initiation {

	public static void init(Activity activity){
		MyContext.getInstance().setApp(activity.getApplication());
		//异常处理
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(MyContext.getInstance().getApp());
	}
}
