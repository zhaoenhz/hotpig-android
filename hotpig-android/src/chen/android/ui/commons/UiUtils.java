package chen.android.ui.commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import chen.android.core.Authorization;
import chen.android.core.MyContext;
import chen.android.utils.Pairs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.Toast;

public final class UiUtils {
	
    public static void exitDialog(final Activity context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确定要注销账号吗?");
		builder.setTitle("提示");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确认",
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Authorization.cancelAuth();
				dialog.dismiss();
				context.startActivity(IntentGenerator.exitActivity());
			}
		});
		builder.setNegativeButton("取消",
		new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
    }
    
    public static void dialog(final Activity context, String message, OnClickListener listener){
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle("提示");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确认",listener);
		builder.setNegativeButton("取消",
		new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
    }
    
    /**
     * 确定是否要放弃编辑，判断输入框中是否有内容，若有则弹出确认框，返回true, 否则返回false
     * @param context
     * @param message
     * @param views
     * @return
     */
    public static boolean confirmGiveUpEdit(final Activity context, String message, final EditText ... views){
    	boolean hasContent = false;;
    	for(EditText et : views){
    		if(et.getText().toString().trim().length() > 0){
    			hasContent = true;
    		}
    	}
		if(hasContent){
			UiUtils.dialog(context, message, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					for(EditText et : views){
						et.setText("");
					}
					context.onBackPressed();
				}
			});
			return true;
		}
		return false;
    }
    
    public static void restartDialog(String message){
    	final Activity context = MyContext.getInstance().getCurrentActivity();
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle("提示");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setPositiveButton("确认",
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
		        Intent i = context.getBaseContext().getPackageManager()  
		                .getLaunchIntentForPackage(context.getBaseContext().getPackageName());  
		        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		        context.startActivity(i);  
			}
		});
		builder.setNegativeButton("取消",
		new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
    }
    
    public static Pairs<String, String> dataFormat(long timestamp){
    	Pairs<String, String> result = new Pairs<String, String>();
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeInMillis(timestamp);
		result.b = (cal.get(Calendar.HOUR_OF_DAY)<10?"0"+cal.get(Calendar.HOUR_OF_DAY):cal.get(Calendar.HOUR_OF_DAY)) + ":" + 
					(cal.get(Calendar.MINUTE)<10?"0"+cal.get(Calendar.MINUTE):cal.get(Calendar.MINUTE));
		
		Calendar today = earliest(Calendar.getInstance());
		cal = earliest(cal);
		if(today.getTimeInMillis()==cal.getTimeInMillis()) result.a = "今天";
		else if(today.getTimeInMillis()-cal.getTimeInMillis()==24*3600*1000) result.a = "昨天";
		else if(today.getTimeInMillis()-cal.getTimeInMillis()==2*24*3600*1000) result.a = "前天";
		else {
			result.a = (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		}
		return result;
    }
    
    public static void toast(Exception ex){
        toast(ex.getMessage());	
    }
    
    public static void toast(final String message){
    	 Runnable task = new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(MyContext.getInstance().getApp(), message, Toast.LENGTH_LONG).show();
			}
    	 };
    	 new Handler(Looper.getMainLooper()).post(task);
    }
    
    private  static Calendar earliest(Calendar cal){
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	return cal;
    }
    
    
	
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	public static int dp2px(int dp){
		DisplayMetrics displayMetrics = MyContext.getInstance().getApp().getResources().getDisplayMetrics();
		return (int)((dp * displayMetrics.density) + 0.5);
	}
	
	public static int px2dp(int px){
		DisplayMetrics displayMetrics = MyContext.getInstance().getApp().getResources().getDisplayMetrics();
		return (int) ((px/displayMetrics.density)+0.5);
	}
	
	public static float getRawSize(int unit, float size) {
	    return TypedValue.applyDimension(unit, size, MyContext.getInstance().getApp().getResources().getDisplayMetrics());
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Uri uri, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try{
			InputStream is = MyContext.getInstance().getApp().getContentResolver().openInputStream(uri);
			BitmapFactory.decodeStream(is, null, options);	
			is.close();
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			options.inJustDecodeBounds = false;
			is = MyContext.getInstance().getApp().getContentResolver().openInputStream(uri);
			Bitmap bm = BitmapFactory.decodeStream(is, null, options);
			is.close();
			return bm;
		}catch(IOException ex){
			throw new RuntimeException(ex);
		}
    }

}
