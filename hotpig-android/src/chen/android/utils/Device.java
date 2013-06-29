package chen.android.utils;

import chen.android.core.MyContext;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

public class Device {
	
	
	private static Application context(){
		return MyContext.getInstance().getApp();
	}
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public static boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) context().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	
	
	public static boolean isSdcardWriteable() {  
        final String state = Environment.getExternalStorageState();  
        if (state.equals(Environment.MEDIA_MOUNTED)) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
	
	public static boolean isSilentMode(){
		AudioManager am = (AudioManager)context().getSystemService(Context.AUDIO_SERVICE);
		return am.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
	}
	
	private static MediaPlayer mCurrentMediaPlayer;
	public static void playSound(int resId) {
		// Stop current player, if there's one playing
		if (null != mCurrentMediaPlayer) {
			mCurrentMediaPlayer.stop();
			mCurrentMediaPlayer.release();
		}

		mCurrentMediaPlayer = MediaPlayer.create(context(), resId);
		if (null != mCurrentMediaPlayer) {
			mCurrentMediaPlayer.start();
		}
	}
	
	@SuppressLint("NewApi")
	public static Point getSreenSize(){
		WindowManager wm = (WindowManager) MyContext.getInstance().getApp().getSystemService(Context.WINDOW_SERVICE);
	    Point size = new Point();
	    Display d = wm.getDefaultDisplay();
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	        d.getSize(size);
	    } else {
	        size.x = d.getWidth();
	        size.y = d.getHeight();
	    }
	    return size;		
	} 

}
