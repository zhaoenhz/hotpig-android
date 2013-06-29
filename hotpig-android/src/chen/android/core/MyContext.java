package chen.android.core;

import java.io.File;

import chen.android.data.Account;
import chen.android.ui.MainActivity;
import chen.android.utils.Device;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.preference.PreferenceManager;


public class MyContext {
	
	private MyContext(){}
	private Application app;
	
	public static final String BaseUrl = "http://www.17daka.com";

	private static MyContext context = new MyContext();
	public static MyContext getInstance(){
		return context;
	}
	
	public Application getApp() {
		return app;
	}

	void setApp(Application app) {
		this.app = app;
	}
	
	public float getAppVersion(){
		return Float.valueOf(getPackageInfo().versionName.trim());
	}
	
	/**
	 * 判断应用是否为第一次运行，应用更新后第一次运行也返回true
	 * @return
	 */
	public boolean isFirstRun(){
		String key = "preference_first_run"+String.valueOf(getAppVersion());
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences (getApp());
		boolean firstRun = p.getBoolean(key, true);
		if(firstRun){
			p.edit().putBoolean(key, false).commit();
		}
		return firstRun;
	}
	
	public void resetFirstRunFlag(){
		String key = "preference_first_run"+String.valueOf(getAppVersion());
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences (getApp());
		p.edit().putBoolean(key, true).commit();
	}
	
	public PackageInfo getPackageInfo(){
		PackageInfo info;
		try {
			info = getApp().getPackageManager().getPackageInfo(getApp().getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return info;
	}
	
	
	public File getCacheDir(){
		File cacheDir = null;
		if(Device.isSdcardWriteable()){
			File sdcardDir =Environment.getExternalStorageDirectory();
			String path = sdcardDir.getPath() + File.separator +  "wedream" + File.separator +"mycache";
			cacheDir = new File(path);
			if(!cacheDir.exists()){
				cacheDir.mkdirs();
			}
		} else {
			File cache = getApp().getCacheDir();
			File appDir = new File(cache.getParent());
			cacheDir = new File(appDir.getPath() +  File.separator + "mycache");
		}
		return cacheDir;
	}
	
	public Account getAccount(){
		return Authorization.getAuthAccount();
	}

	private Activity activity;
	/**
	 * 需要保证首次调用此方法时设置的当前activity是MainActivity，否则会被finish
	 * 当退出时需要设置当前activity为null，这样可以finish栈中所有的acivity
	 * 此方法一般在Activity的onResume方法中调用
	 * @param activity
	 */
	public void setCurrentActivity(Activity activity){
		if(this.activity == null && !(activity instanceof MainActivity)){
			activity.finish();
		} else {
			this.activity = activity;
		}
	}
	
	public Activity getCurrentActivity(){
		return activity;
	}
}
