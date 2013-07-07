package chen.android.data.cache;

import java.util.LinkedHashMap;

public class LRUCache<T> extends LinkedHashMap<String,T>{

	private static final long serialVersionUID = 1L;
	
	private int maxSize;
	public LRUCache(int maxSize){
		this.maxSize = maxSize;
	}
	
	@Override
	protected boolean removeEldestEntry(Entry<String, T> eldest) {
		// TODO Auto-generated method stub
		return super.size() > maxSize;
	}
}
