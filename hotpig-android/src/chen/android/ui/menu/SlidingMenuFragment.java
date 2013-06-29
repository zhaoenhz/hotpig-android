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
			
			getView().findViewById(R.id.dream_info).setVisibility(LinearLayout.VISIBLE);
			TextView totalDream = (TextView)getView().findViewById(R.id.total_dream);
			TextView totalAsk = (TextView)getView().findViewById(R.id.total_ask);
			TextView totalFollowing = (TextView)getView().findViewById(R.id.total_following);
			totalDream.setText(countHtml("分享", 0));
			totalAsk.setText(countHtml("问梦", 0));
			totalFollowing.setText(countHtml("好友", 0));
			
			totalDream.setOnClickListener(listener);
			totalAsk.setOnClickListener(listener);
			totalFollowing.setOnClickListener(listener);
		} else {
			nickname.setText("账号登录");
			avatar.setImageResource(R.drawable.ic_search);
		}
	}
	
	private Spanned countHtml(String type, int count){
		return Html.fromHtml("<font color=\"white\">"+ count +"</font><br/><font color=\"#999999\">"+ type + "</font>");
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventSupport.remove(this);
		super.onDestroy();
	}
	
}
