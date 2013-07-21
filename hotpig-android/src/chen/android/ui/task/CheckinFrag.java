package chen.android.ui.task;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import chen.android.F;
import chen.android.R;
import chen.android.data.Checkin;
import chen.android.data.Star;
import chen.android.exception.HotpigException;
import chen.android.ui.commons.BackPressedHandler;
import chen.android.ui.commons.IntentGenerator;
import chen.android.ui.commons.LoadingAsyncTask;
import chen.android.ui.commons.UiUtils;
import chen.android.ui.my.MySherlockListFragment;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class CheckinFrag extends MySherlockListFragment implements BackPressedHandler{
	
	public static final String KeyTaskId = "task-id";
	
	private EditText contentView;
	private RatingBar scoreView;
	private int taskId;
	private PullToRefreshListView ptr;
	private SampleAdapter adapter;
	private View headerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		headerView = inflater.inflate(R.layout.task_checkin_header, null);
		return inflater.inflate(R.layout.commons_pull_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getListView().addHeaderView(headerView, null, false);
		taskId = getSherlockActivity().getIntent().getExtras().getInt(KeyTaskId,0);
		if(taskId == 0) throw new IllegalArgumentException();
		contentView = (EditText) getView().findViewById(R.id.content);
		scoreView = (RatingBar) getView().findViewById(R.id.score);
		View submitBtn = getView().findViewById(R.id.submitBtn);
		ptr = (PullToRefreshListView) getView().findViewById(R.id.pull_refresh_list);
		ptr.setShowIndicator(false);
		adapter = new SampleAdapter();
		setListAdapter(adapter);
		
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = contentView.getText().toString();
				int score = (int) scoreView.getRating();
				if(content.trim().length() == 0){
					UiUtils.toast("请输入内容");
					return;
				}
				runTask(new CheckInTask(taskId, content, Star.values()[score-1]));
			}
		});
		runTask(new ListCheckinTask(taskId));
	}
	
	private class CheckInTask extends LoadingAsyncTask<Void, Void, Boolean>{

		int taskId;
		String content;
		Star star;
		protected CheckInTask(int taskId, String content, Star star) {
			super(getSherlockActivity());
			this.taskId = taskId;
			this.content = content;
			this.star = star;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				F.api().checkIn(taskId, content, star, false);
				return true;
			} catch (HotpigException e) {
				// TODO Auto-generated catch block
				UiUtils.toast(e);
			} 
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				startActivity(IntentGenerator.myTaskList());
			}
		}
		
	}
	
	private class ListCheckinTask extends LoadingAsyncTask<Void, Void, List<Checkin>>{

		private int taskId;
		protected ListCheckinTask(int taskId) {
			super(getSherlockActivity());
			this.taskId = taskId;
		}

		@Override
		protected List<Checkin> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				return F.api().listCheckin(taskId);
			} catch (HotpigException e) {
				// TODO Auto-generated catch block
				UiUtils.toast(e);
			} 
			return null;
		}

		@Override
		protected void onPostExecute(List<Checkin> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result == null) return;
			for(Checkin checkin : result){
				SampleItem item = new SampleItem(checkin);
				adapter.add(item);
			}
		}

	}
	
	private class SampleItem {
		Checkin checkin;
		SampleItem(Checkin checkin){
			this.checkin = checkin;
		}
	}
	
	private class SampleAdapter extends ArrayAdapter<SampleItem>{

		SampleAdapter() {
			super(getSherlockActivity(), 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			SampleItem item = getItem(position);
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_checkin_row, null);
			}
			TextView contentView = (TextView) convertView.findViewById(R.id.row_content);
			contentView.setText(item.checkin.content);
			return convertView;
		}
	}
	
	@Override
	protected String getTitile() {
		// TODO Auto-generated method stub
		return "打卡";
	}

	@Override
	public boolean handleBackPress() {
		// TODO Auto-generated method stub
		return UiUtils.confirmGiveUpEdit(getSherlockActivity(), "确定放弃编辑吗？", contentView);
	}

}
