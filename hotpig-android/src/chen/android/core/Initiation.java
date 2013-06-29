package chen.android.core;

import android.app.Activity;

public class Initiation {

	public static void init(Activity activity){
		MyContext.getInstance().setApp(activity.getApplication());
	}
}
