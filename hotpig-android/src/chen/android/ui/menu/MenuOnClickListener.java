package chen.android.ui.menu;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import chen.android.R;
import chen.android.core.Authorization;
import chen.android.core.MyContext;
import chen.android.ui.commons.IntentGenerator;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuOnClickListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch(v.getId()){
		
		case R.id.header :{
			if(Authorization.hasAuth()){
				intent = IntentGenerator.accountInfo(MyContext.getInstance().getAccount().id);
			} else {
				intent = IntentGenerator.signIn();
			}
			break;
		}
		
		case ListAdapterGenerator.IdTaskList :{
			intent = IntentGenerator.myTaskList();
			break;
		}
		
		case ListAdapterGenerator.IdExit :{
			intent = IntentGenerator.exitActivity();
			break;
		}
		
		}
		
		
		if(intent != null){
			Context context = v.getContext();
			if(context instanceof SlidingFragmentActivity && v.getId() != ListAdapterGenerator.IdExit){
				SlidingFragmentActivity sliding = (SlidingFragmentActivity) context;
				sliding.toggle();
			}
			context.startActivity(intent);
		}
	}

}
