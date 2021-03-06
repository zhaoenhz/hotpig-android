package chen.android.ui.task;

import java.text.SimpleDateFormat;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import chen.android.F;
import chen.android.R;
import chen.android.core.MyContext;
import chen.android.data.CheckinStatus;
import chen.android.data.Star;
import chen.android.data.TaskThread;
import chen.android.exception.HotpigException;
import chen.android.ui.commons.ChartView;
import chen.android.ui.commons.IntentGenerator;
import chen.android.ui.commons.LoadingAsyncTask;
import chen.android.ui.commons.UiUtils;
import chen.android.ui.my.MySherlockListFragment;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class TaskListFrag extends MySherlockListFragment implements OnRefreshListener<ListView> {

	private PullToRefreshListView ptr;
	private SampleAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.commons_pull_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		ptr = (PullToRefreshListView) getView().findViewById(R.id.pull_refresh_list);
		ptr.setOnRefreshListener(this);
		ptr.setShowIndicator(false);
		adapter = new SampleAdapter();
		setListAdapter(adapter);
		runTask(new ListTask(MyContext.getInstance().getAccount().id));
	}
	
	private class ListTask extends LoadingAsyncTask<Void, Void, List<TaskThread>>{

		private int id;
		
		protected ListTask(int id) {
			super(getSherlockActivity());
			this.id = id;
		}

		@Override
		protected List<TaskThread> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			try {
				return F.api().listTask(id);
			} catch (HotpigException e) {
				// TODO Auto-generated catch block
				UiUtils.toast(e);
			} 
			return null;
		}

		@Override
		protected void onPostExecute(List<TaskThread> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ptr.onRefreshComplete();
			if(result == null) return;
			adapter.clear();
			for(TaskThread task : result){
				SampleItem item = new SampleItem(task);
				adapter.add(item);
			}
		}

	}
	
	private class SampleItem{
		TaskThread task;
		OnClickListener listener;
		ChartView chartView;
		
		SampleItem(final TaskThread task){
			this.task = task;
			listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch(v.getId()){
					case R.id.row_task : {
						startActivity(IntentGenerator.checkIn(task.id));
						break;
					}
					
					case R.id.done : {
						CheckinStatus status = new CheckinStatus();
						status.star = Star.five;
						status.failed = false;
						status.progress = task.baseProgress + task.progress + 1;
						runTask(new CheckinTask(task, status, chartView));
						break;
					}
					
					case R.id.failed : {
						CheckinStatus status = new CheckinStatus();
						status.failed = true;
						status.progress = task.baseProgress + task.progress + 1;
						runTask(new CheckinTask(task, status, chartView));
						break;
					}
					}
				}
			};
		}
	}
	
	private class CheckinTask extends LoadingAsyncTask<Void, Void, Integer>{

		TaskThread task;
		CheckinStatus checkinStatus;
		ChartView chartView;
		protected CheckinTask(TaskThread task, CheckinStatus checkinStatus, ChartView chartView) {
			super(getSherlockActivity());
			this.task = task;
			this.chartView = chartView;
			this.checkinStatus = checkinStatus;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				checkinStatus.id = F.api().checkIn(task.id, "", checkinStatus.star, checkinStatus.failed);
				return checkinStatus.id;
			} catch (HotpigException e) {
				// TODO Auto-generated catch block
				UiUtils.toast(e);
			} 
			return null;
		}

		@Override
		protected void onPostExecute(Integer id) {
			// TODO Auto-generated method stub
			super.onPostExecute(id);
			if(id == null) return;
			CheckinStatus latest = task.statusList.get(task.statusList.size()-1);
			if(latest.id == id){
				task.statusList.remove(task.statusList.size()-1);
				task.statusList.add(checkinStatus);
			} else {
				task.statusList.add(checkinStatus);
			}
			chartView.setCheckinStatus(task.statusList);
		}
	}
	
	private class SampleAdapter extends ArrayAdapter<SampleItem>{

		public SampleAdapter() {
			super(getSherlockActivity(), 0);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			SampleItem item = getItem(position);
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_row, null);
			}
			TextView titleView = (TextView)convertView.findViewById(R.id.row_title);
			TextView dateView = (TextView)convertView.findViewById(R.id.row_date);
			ChartView chartView = (ChartView) convertView.findViewById(R.id.chart);
			Button doneBtn = (Button) convertView.findViewById(R.id.done);
			Button failedBtn = (Button) convertView.findViewById(R.id.failed);
			
			chartView.setCheckinStatus(item.task.statusList);
			item.chartView = chartView;
			titleView.setText(item.task.title);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dateView.setText(sdf.format(item.task.schedule.getStartTime()) +  " 至 " + sdf.format(item.task.schedule.getEndTime()));
			
			convertView.setOnClickListener(item.listener);
			doneBtn.setOnClickListener(item.listener);
			failedBtn.setOnClickListener(item.listener);
			return convertView;
		}

	}

	@Override
	protected String getTitile() {
		// TODO Auto-generated method stub
		return "我的任务";
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		runTask(new ListTask(MyContext.getInstance().getAccount().id));
	}
}
