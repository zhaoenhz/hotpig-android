package chen.android.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import chen.android.R;
import chen.android.ui.my.MySherlockListFragment;

public class TaskShowFrag extends MySherlockListFragment{

	@Override
	protected String getTitile() {
		// TODO Auto-generated method stub
		return "查看任务";
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.commons_pull_list, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
	}

}
