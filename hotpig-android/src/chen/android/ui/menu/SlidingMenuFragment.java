package chen.android.ui.menu;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import chen.android.R;
import chen.android.core.Authorization;
import chen.android.core.MyContext;
import chen.android.data.Account;
import chen.android.event.Event;
import chen.android.event.EventListener;
import chen.android.event.EventSupport;
import chen.android.event.EventType;
import chen.android.ui.commons.AvatarDrawable;

import com.actionbarsherlock.app.SherlockListFragment;

public class SlidingMenuFragment extends SherlockListFragment implements EventListener{
	
	/**
	 * 是否需要刷新标记，当用户登录后，或者改了资料，需要做刷新操作
	 */
	private boolean needRefresh = false;
	private ListAdapterGenerator listAdapterGenerator = null;
	private MenuOnClickListener listener = new MenuOnClickListener();
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listAdapterGenerator = new ListAdapterGenerator(this.getSherlockActivity(), listener);
		refreshView(true);
		EventSupport.register(this);
	}
	
	@Override
	public void listen(Event event) {
		// TODO Auto-generated method stub
		if(event.getType() == EventType.Login || event.getType() == EventType.UpdateAccountInfo){
			needRefresh = true;	
		}
	}
	
	/**
	 * 刷新界面
	 * @param force 是否强制刷新
	 */
	private void refreshView(boolean force){
		if(force || needRefresh){
			boolean auth = Authorization.hasAuth();
			setHeader(auth);
			this.setListAdapter(listAdapterGenerator.generate(auth));	
			needRefresh = false;
		}
	}
	
	public void refreshView(){
		refreshView(false);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list, null);
	}
	
	private void setHeader(boolean auth){
		ImageView avatar = (ImageView)getView().findViewById(R.id.avatar);
		TextView nickname = (TextView)getView().findViewById(R.id.nickname);
		LinearLayout header = (LinearLayout)getView().findViewById(R.id.header);
		header.setOnClickListener(listener);
		if(auth){
			Account acc = MyContext.getInstance().getAccount();
			avatar.setImageDrawable(new AvatarDrawable(avatar, acc.id));
			nickname.setText(acc.nickname);
		} else {
			nickname.setText("账号登录");
			avatar.setImageResource(R.drawable.ic_search);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventSupport.remove(this);
		super.onDestroy();
	}
	
}
