package chen.android.ui.commons;
import chen.android.F;
import chen.android.exception.FailureResponseException;
import chen.android.exception.NetworkException;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 可以异步加载头像
 *
 */
public class AvatarDrawable extends BitmapDrawable{

	private int avatarId;
	private ImageView container;
	
	/**
	 * @param container	需要设置tag为头像id
	 * @param avatarId
	 */
	public AvatarDrawable(ImageView container, int avatarId){
		super(container.getResources());
		this.container = container;
		this.avatarId = avatarId;
		container.setTag(avatarId);
		new LoadAvatarTask().execute();
	}
    
	private boolean match(){
		return avatarId == (Integer)container.getTag();
	}
    
    private class LoadAvatarTask extends AsyncTask<Void, Void, Bitmap>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(!match()){
				super.cancel(true);
			}
		}
    	
		@Override
		protected Bitmap doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(!match()){
				super.cancel(true);
			}
			try {
				return F.api().loadAvatar(avatarId);
			} catch (NetworkException e) {
				// TODO Auto-generated catch block
			} catch (FailureResponseException e) {
				// TODO Auto-generated catch block
				if(e.getStatusCode() == 404){
					return StaticResourceProvider.defaultAvatar();
				} else {
					UiUtils.toast(e);
				}
			} 
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result != null && match()){
				container.setImageDrawable(new BitmapDrawable(container.getResources(), result));
			}
		}

    }
}
