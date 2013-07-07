package chen.android.data;

import java.util.Date;


public class Branch{

	public static enum Visible {Public,Friendly,Private};
	
	public String description;
	public Account owner;
	public Date createTime;
	public Tag tag;
	public Visible visible = Visible.Public;		
	public boolean hasPartner;		//冗余,是否有搭档
	public int latestTaskId;	//冗余,分支里最新任务的id
	public boolean hasTeam;	//冗余，是否加入了团队
	


}
