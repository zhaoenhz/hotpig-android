package chen.android.ui.my;

import android.os.AsyncTask;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public abstract class MySherlockListFragment extends SherlockListFragment{
	
	protected abstract String getTitile();

	protected void runTask(AsyncTask<Void, ?, ?> task){
		TaskManager.run(task);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		String title = getTitile();
		if(title != null){
			getSherlockActivity().getSupportActionBar().setTitle(getTitile());	
		}	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TaskManager.cancle();
	}
}
