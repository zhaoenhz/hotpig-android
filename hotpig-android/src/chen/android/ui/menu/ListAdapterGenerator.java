package chen.android.ui.menu;

import chen.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

class ListAdapterGenerator {

	private Context context;
	private MenuOnClickListener listener;
	
	public static final int IdTaskList = 1;
	public static final int IdSetting = 2;
	public static final int IdExit = 3;
	
	private SampleItem notificationItem = new SampleItem(0, "提醒", R.drawable.ic_search);
	private SampleItem trendsItem = new SampleItem(0, "好友动态", R.drawable.ic_search);
	private SampleItem exploreItem = new SampleItem(0, "浏览发现", R.drawable.ic_search);
	private SampleItem myTaskListItem = new SampleItem(IdTaskList, "我的任务", R.drawable.ic_search);
	private SampleItem settingItem = new SampleItem(IdSetting, "设置", R.drawable.ic_search);
	private SampleItem exitItem = new SampleItem(IdExit, "退出", R.drawable.ic_search);
	
	ListAdapterGenerator(Context context, MenuOnClickListener listener){
		this.context = context;
		this.listener = listener;
	}
	
	/**
	 * 生成菜单列表
	 * @param auth
	 * @return
	 */
	ListAdapter generate(boolean auth){
		SampleAdapter adapter = new SampleAdapter(context);
		if(auth){
			adapter.add(myTaskListItem);
			adapter.add(exploreItem);
			adapter.add(trendsItem);
			adapter.add(notificationItem);
		} else {
			adapter.add(exploreItem);
		}
		adapter.add(settingItem);
		adapter.add(exitItem);
		return adapter;
	}
	
	
	private class SampleItem {
		int id;
		String tag;
		int iconRes;
		SampleItem(int id, String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
			this.id = id;
		}
	}

	private class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_row, null);
			}
			SampleItem item = getItem(position);
			convertView.setId(item.id);
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(item.iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(item.tag);
			
			convertView.setOnClickListener(listener);
			
			return convertView;
		}

	}
}
