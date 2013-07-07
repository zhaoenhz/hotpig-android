package chen.android.data.cache;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import android.graphics.Bitmap;

import chen.android.F;
import chen.android.core.MyContext;
import chen.android.utils.ParameterizedTypeImpl;

public class FileCacheFactory {
	
	private static LRUCache<Bitmap> bitmapCache = new LRUCache<Bitmap>(50);
	
	private static FileCache<Bitmap> emotCache = new FileCache<Bitmap>(new File(MyContext.getInstance().getCacheDir().getPath() + File.separator + "emot"), new BitmapConverter(), bitmapCache);
	private static FileCache<Bitmap> imageCache = new FileCache<Bitmap>(new File(MyContext.getInstance().getCacheDir().getPath() + File.separator + "image"), new BitmapConverter());
	private static FileCache<Bitmap> avatarCache = new FileCache<Bitmap>(new File(MyContext.getInstance().getCacheDir().getPath() + File.separator + "avatar"),new BitmapConverter(), bitmapCache);
	private static FileCache<String> jsonCache = new FileCache<String>(new File(MyContext.getInstance().getCacheDir().getPath() + File.separator + "json"), new StringConverter());
	
	private static final String EmotPreffix = "emot-";
	public static void putEmot(String key, Bitmap bitmap){
		emotCache.put(EmotPreffix+key, bitmap);
	}
	
	public static Bitmap getEmot(String key){
		return emotCache.get(EmotPreffix+key);
	}
	
	private static final String ImagePreffix = "img-";
	public static void putImage(int imageId, Bitmap bitmap){
		imageCache.put(ImagePreffix+String.valueOf(imageId), bitmap);
	}
	
	public static Bitmap getImage(int imageId){
		return imageCache.get(ImagePreffix+String.valueOf(imageId));
	}
	
	private static final String AccountPreffix = "acc-";
	public static Bitmap getAvatar(int accountId){
		return avatarCache.get(AccountPreffix+String.valueOf(accountId));
	}
	
	public static void putAvatar(int accountId, Bitmap data){
		avatarCache.put(AccountPreffix +String.valueOf(accountId), data);
	}
	
	public static void removeAvatar(int accountId){
		avatarCache.remove(AccountPreffix + String.valueOf(accountId));
	}
	
	private static final String JsonPreffix = "json-"; 
	public static void putJson(String key, Object obj){
		jsonCache.put(JsonPreffix+key, F.gson().toJson(obj));
	}
	
	public static <T> List<T> getJsonArray(String key, Class<T> cls){
		String json = jsonCache.get(JsonPreffix+key);
		if(json == null) return Collections.emptyList();
		return F.gson().fromJson(json, new ParameterizedTypeImpl(List.class, new Type[]{cls}, null));
	}
	
	public static <T> T getJson(String key, Class<T> cls){
		String json = jsonCache.get(JsonPreffix+key);
		if(json == null) return null;
		return F.gson().fromJson(json, cls);
	}
	
	public static void clearAll(){
		imageCache.clear();
		avatarCache.clear();
		emotCache.clear();
		jsonCache.clear();
	}
	
}
