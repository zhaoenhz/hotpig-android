package chen.android.ui.commons;

import chen.android.core.MyContext;
import chen.android.ui.MainActivity;
import chen.android.ui.account.SignInFrag;
import chen.android.ui.task.CheckinFrag;
import chen.android.ui.task.TaskListFrag;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class IntentGenerator {

	public static Intent exitActivity(){
		Intent intent = new Intent(MyContext.getInstance().getCurrentActivity(), MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 
		intent.putExtra(MainActivity.KeyExit, true);
		return intent;
	}
	
	public static Intent accountInfo(int id){
		return null;
	}
	
	public static Intent signIn(){
		return mainActivity(SignInFrag.class);
	}
	
	public static Intent myTaskList(){
		return mainActivity(TaskListFrag.class);
	}
	
	public static Intent checkIn(int taskId){
		return mainActivity(CheckinFrag.class).putExtra(CheckinFrag.KeyTaskId, taskId);
	}

	private static Intent mainActivity(Class<? extends Fragment> cls){
		Intent intent = new Intent(MyContext.getInstance().getCurrentActivity(), MainActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);   
    	String key = cls.getName();
    	intent.putExtra(MainActivity.FragmentKey, key);
    	return intent;
	}
}
