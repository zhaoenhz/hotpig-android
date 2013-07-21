package chen.android.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * 此时间表不能有任一重叠的时间安排
 * @author admin
 *
 */
public class Schedule {
	
	public List<ScheduleNode> nodes = new ArrayList<ScheduleNode>();
	
	public Date getStartTime(){
		return nodes.get(0).timeSegment.getBeginTime();
	}
	
	public Date getEndTime(){
		return nodes.get(nodes.size()-1).timeSegment.getEndTime();
	}
}
