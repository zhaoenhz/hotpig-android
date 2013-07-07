package chen.android.ui.commons;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import chen.android.R;
import chen.android.core.MyContext;

public class StaticResourceProvider {

	private static Bitmap bitmap;
	public static Bitmap defaultAvatar(){
		if(bitmap == null){
			BitmapDrawable drawable = (BitmapDrawable)MyContext.getInstance().getApp().getResources().getDrawable(R.drawable.avatar_default);
			bitmap = drawable.getBitmap();	
		}
		return bitmap;
	}
}
