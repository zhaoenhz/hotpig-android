package chen.android.ui.my;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;

class TaskManager {

	private static Map<String,AsyncTask<Void, ?, ?>> mapping = new ConcurrentHashMap<String,AsyncTask<Void, ?, ?>>();
	
	public static void run(AsyncTask<Void, ?, ?> task){
		String key = key(task);
		AsyncTask<Void, ?, ?> old = mapping.get(key);
		if(old == null || old.getStatus() == Status.FINISHED){
			mapping.put(key, task);
			task.execute();	
		}
	}
	
	public static void cancle(){
		Set<Entry<String,AsyncTask<Void, ?, ?>>> entries = mapping.entrySet();
		for(Entry<String,AsyncTask<Void, ?, ?>> entry : entries){
			AsyncTask<Void, ?, ?> task = entry.getValue();
			if(task != null && task.getStatus() != Status.FINISHED){
				task.cancel(true);
			}
		}
		mapping.clear();
	}
	
	private static String key(AsyncTask<Void, ?, ?> task){
		return task.getClass().getName();
	}
}
