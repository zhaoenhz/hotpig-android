package chen.android.event;

public class Event {

	private int type;
	private Object source;
	
	public Event(int type, Object source){
		this.type = type;
		this.source = source;
	}
	
	public int getType() {
		return type;
	}
	public Object getSource() {
		return source;
	}

}
