package chen.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import chen.android.R;
import chen.android.core.Initiation;
import chen.android.core.MyContext;
import chen.android.ui.menu.SlidingMenuFragment;
import chen.android.ui.task.TaskListFrag;
import chen.android.ui.commons.*;

import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainActivity extends SlidingFragmentActivity implements ActivityResultSetter{

	public static final String FragmentUseBackStackKey = "back-stack";
	public static final String FragmentKey = "fragment";
	
	private Intent intent;
	
	@Override
	public void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		Initiation.init(this);
		super.setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);
		super.onCreate(bundle);
		setContentView(R.layout.main_activity);
		initSlidingMenu(bundle);
		this.intent = super.getIntent();
		intent.putExtra(FragmentKey, TaskListFrag.class.getName());
		try {
			handleIntent();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		this.intent = intent;
		try {
			handleIntent();
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MyContext.getInstance().setCurrentActivity(this);
	}
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(this.getSlidingMenu().isMenuShowing()){
			finish();
		} else {
			getSlidingMenu().showMenu();
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			onBackPressed();
			return true;
		} 
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void finish(){
		super.finish();
		MyContext.getInstance().setCurrentActivity(null);
	}
	
	private ActivityResultHandler resultHandler;
	@Override
	public void setActivityResultHandler(ActivityResultHandler resultHandler) {
		// TODO Auto-generated method stub
		this.resultHandler = resultHandler;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultHandler != null) resultHandler.handle(requestCode, resultCode, data);
	}


	private void handleIntent() throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String fragName = intent.getExtras().getString(FragmentKey);
		FragmentManager fm = getSupportFragmentManager();
		
		if(intent.getExtras().getBoolean(FragmentUseBackStackKey) && fm.getBackStackEntryCount() > 0){
			fm.popBackStackImmediate(fragName, 0);
			Fragment currentFrag = fm.findFragmentById(R.id.my_fragment);
			//在历史记录中找到，不需要再次创建fragment
			if(currentFrag != null && currentFrag.getClass().getName().equals(fragName)){
				if(currentFrag instanceof OnNewIntent4Frag){
					((OnNewIntent4Frag)currentFrag).onNewIntent(intent);
				}
				return;
			}
		}
		
		
		Fragment currentFrag = (Fragment)Class.forName(fragName).newInstance();

		currentFrag.setHasOptionsMenu(true);
		
		FragmentTransaction ft = fm.beginTransaction();
		Fragment old = fm.findFragmentById(R.id.my_fragment);
		if(old != null) ft.hide(old);
		ft.add(R.id.my_fragment, currentFrag, null).addToBackStack(fragName).commitAllowingStateLoss();
	}
	

	private SlidingMenuFragment mFrag;
	private void initSlidingMenu(Bundle savedInstanceState){
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new SlidingMenuFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (SlidingMenuFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setBehindWidth(360);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		sm.setOnOpenedListener(new OnOpenedListener() {
			@Override
			public void onOpened() {
				// TODO Auto-generated method stub
				getSupportActionBar().setDisplayHomeAsUpEnabled(false);
				mFrag.refreshView();
			}
		});
		sm.setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		});
	}
	
	
	
}
