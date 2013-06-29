package chen.android.ui.commons;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import android.app.Activity;
import android.os.AsyncTask;

public abstract class LoadingAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{

	protected Activity activity;
	protected LoadingAsyncTask(Activity activity){
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		loading(true);
	}

	@Override
	protected void onPostExecute(Result result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		loading(false);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		loading(false);
	}

	private void loading(boolean visible){
		if(activity instanceof SherlockFragmentActivity){
			setMenuVisible(!visible);
			SherlockFragmentActivity fa = (SherlockFragmentActivity) activity;
			fa.setSupportProgressBarIndeterminateVisibility(visible);
		}
	}
	
	private void setMenuVisible(boolean visible){
		if(activity instanceof MenuProvider){
			Menu menu = ((MenuProvider)activity).getMenu();
			if(menu == null) return;
			int size = menu.size();
			try{
				for(int i=0; i<size; i++){
					menu.getItem(i).setVisible(visible);
				}	
			}catch(Exception ex){}
		}
	}


}
