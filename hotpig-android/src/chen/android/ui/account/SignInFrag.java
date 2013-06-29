package chen.android.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import chen.android.F;
import chen.android.R;
import chen.android.core.Authorization;
import chen.android.data.Account;
import chen.android.exception.HotpigException;
import chen.android.ui.commons.IntentGenerator;
import chen.android.ui.commons.LoadingAsyncTask;
import chen.android.ui.commons.UiUtils;
import chen.android.ui.my.MySherlockFragment;

public class SignInFrag extends MySherlockFragment{

	private EditText emailView;
	private EditText passwordView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.sign_in, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		emailView = (EditText) getView().findViewById(R.id.email);
		passwordView = (EditText) getView().findViewById(R.id.password);
		View signInView = getView().findViewById(R.id.signInBtn);
		
		MyOnClickListener listener = new MyOnClickListener();
		signInView.setOnClickListener(listener);
	}
	
	private class MyOnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.signInBtn : {
				String email = emailView.getText().toString().trim();
				String password = passwordView.getText().toString();
				if(email.length() == 0){
					UiUtils.toast("请输入账号电邮地址");
				} else if(password.length() == 0){
					UiUtils.toast("请输入密码");
				}
				runTask(new SignUpTask(email, password));
			}
			}
		}
	}

	private class SignUpTask extends LoadingAsyncTask<Void, Void, Boolean>{

		private String email;
		private String password;
		protected SignUpTask(String email, String password) {
			super(getSherlockActivity());
			this.email = email;
			this.password = password;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String cookie = F.api().signIn(email, password);
				Account acc = F.api().currentAccount(cookie);
				Authorization.persistAuthCookie(acc, cookie);
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
		return "登录";
	}
}
