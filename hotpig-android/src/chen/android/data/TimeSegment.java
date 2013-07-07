package chen.android.data;

import java.util.Date;

public class TimeSegment{

	private Date beginTime;
	private Date endTime;
	
	TimeSegment(){}
	
	public TimeSegment(Date beginTime, Date endTime){
		if(!beginTime.before(endTime)){
			throw new IllegalArgumentException("结束时间不能比开始时间晚");
		}
		this.beginTime = beginTime;
		this.endTime = endTime;
	}
	
	public Date getBeginTime() {
		return beginTime;
	}

	void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 与给定的时间比较，若给定时间在此时间段内返回0,若在此时间段之前，返回-1,若在此时间段后,返回1
	 * @param date
	 * @return
	 */
	int compareTo(Date date){
		int n = endTime.compareTo(date);
		//在结束时间之后
		if(n<0) return 1;
		
		n = beginTime.compareTo(date);
		//在开始时间之前
		if(n>0) return -1;
		
		//在此时间段中
		return 0;
	}
	
	/**
	 * 比较两个时间段，若给定的时间段在此时间段之前，返回-1;若给定的时间段在此时间段之后，返回1；否则返回0;
	 * 返回0并不表示两个时间段是同一时间段，而是表示两个时间段有重叠部分
	 * @param timeSegment
	 * @return
	 */
	public int compareTo(TimeSegment timeSegment){
		int n1 = compareTo(timeSegment.getBeginTime());
		int n2 = compareTo(timeSegment.getEndTime());
		return n1==-1 && n2==-1 ? -1 : n1==1 && n2==1 ?1 : 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((beginTime == null) ? 0 : beginTime.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSegment other = (TimeSegment) obj;
		if (beginTime == null) {
			if (other.beginTime != null)
				return false;
		} else if (!beginTime.equals(other.beginTime))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		return true;
	}

	
}
