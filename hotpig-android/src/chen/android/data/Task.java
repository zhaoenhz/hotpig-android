package chen.android.data;

import java.util.Date;

public class Task {
	public static final int ScheduleNodesLimited = 90;

	public int id;
	public String title;
	public String description;
	public Schedule schedule;
	public Account creator;
	public Date createTime;
	public int progress;
	public int baseProgress;
	public Branch branch;
	public int commentCount;
	
	
}
