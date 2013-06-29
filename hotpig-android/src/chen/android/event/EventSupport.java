package chen.android.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventSupport {

	private static List<EventListener> Listeners = new  CopyOnWriteArrayList<EventListener>();
	
	public static void register(EventListener listener){
		Listeners.add(listener);
	}
	
	public static void remove(EventListener listener){
		Listeners.remove(listener);
	}
	
	public static void fire(Event event){
		for(EventListener listener : Listeners){
			listener.listen(event);
		}
	}
}
