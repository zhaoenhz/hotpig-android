package chen.android.data.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import chen.android.utils.Utils;

/**
 * 这是一个基于文件的缓存，在内部有一个指定大小的LUR缓存在于内存中，以达到加速目的
 *
 */
class FileCache<T> {

	private File cacheDir;
	private Converter<T> converter;
	private LRUCache<T> memeryCache = null;
	
	FileCache(File cacheDir, Converter<T> converter){
		this(cacheDir, converter, null);
	}
	
	FileCache(File cacheDir, Converter<T> converter, LRUCache<T> memeryCache){
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		this.cacheDir = cacheDir;
		this.memeryCache = memeryCache;
		this.converter = converter;
	} 
	
	private boolean hasMemeryCache(){
		return memeryCache != null;
	}
	
	public void put(String key, T t){
		byte[] buf = new byte[512];
		File file = new File(cacheDir, key);
		try{
			InputStream is = converter.convert(t);
			FileOutputStream fos = new FileOutputStream(file);
			for(int i=is.read(buf); i!=-1; i=is.read(buf)){
				fos.write(buf, 0, i);
			}
			fos.close();
			is.close();
		}catch(IOException ex){
			throw new RuntimeException(ex);
		} 
		if(hasMemeryCache()){
			memeryCache.put(key, t);
		}
	}
	
	public T get(String key){
		if(hasMemeryCache()){
			T val = memeryCache.get(key);
			if(val != null) return val;
		}
		File file = new File(cacheDir, key);
		T val = converter.convert(file);
		if(val != null){
			memeryCache.put(key, val);
		}
		return val;
	}
	
	
	public void remove(String key){
		new File(cacheDir, key).delete();
		if(hasMemeryCache()){
			memeryCache.remove(key);
		}
	}
	
	public void clear(){
		Utils.deleteDir(cacheDir);
		if(hasMemeryCache()){
			memeryCache.clear();
		}
	}
	
}
