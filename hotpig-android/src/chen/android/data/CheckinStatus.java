package chen.android.data;

import java.util.Date;

public class CheckinStatus {

	public int id;
	public boolean failed;
	public Star star;
	public Date createTime;
	//branch progress
	public int progress;
	
	public boolean isEmpty(){
		return this.id == 0 ? true:false;
	}
}
