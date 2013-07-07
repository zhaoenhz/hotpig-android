package chen.android.ui.task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import chen.android.F;
import chen.android.R;
import chen.android.exception.HotpigException;
import chen.android.ui.commons.BackPressedHandler;
import chen.android.ui.commons.IntentGenerator;
import chen.android.ui.commons.LoadingAsyncTask;
import chen.android.ui.commons.UiUtils;
import chen.android.ui.my.MySherlockFragment;

public class CheckinFrag extends MySherlockFragment implements BackPressedHandler{
	
	public static final String KeyTaskId = "task-id";
	
	private EditText contentView;
	private RatingBar scoreView;
	private int taskId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.task_checkin, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		taskId = getSherlockActivity().getIntent().getExtras().getInt(KeyTaskId,0);
		if(taskId == 0) throw new IllegalArgumentException();
		contentView = (EditText) getView().findViewById(R.id.content);
		scoreView = (RatingBar) getView().findViewById(R.id.score);
		View submitBtn = getView().findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = contentView.getText().toString();
				int score = (int) scoreView.getRating();
				if(content.trim().length() == 0){
					UiUtils.toast("请输入内容");
					return;
				}
				runTask(new CheckInTask(taskId, content, score));
			}
		});
	}
	
	private class CheckInTask extends LoadingAsyncTask<Void, Void, Boolean>{

		int taskId;
		String content;
		int star;
		protected CheckInTask(int taskId, String content, int star) {
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
