package chen.android;

import java.lang.reflect.Type;
import java.util.Date;

import chen.android.data.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

//factory
public class F {

	private static Api cacheApi;
	private static Api api;
	private static Gson gson;
	
	static{
		//初始api
		api = new Api(false);
		cacheApi = new Api(true);
		//初始化gson实例 
		GsonBuilder builder = new GsonBuilder(); 
		builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(JsonElement arg0, Type arg1,
					JsonDeserializationContext arg2) throws JsonParseException {
				return new Date(arg0.getAsJsonPrimitive().getAsLong()); 
			} 
		});
		builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(Date arg0, Type arg1,
					JsonSerializationContext arg2) {
				// TODO Auto-generated method stub
				return new JsonPrimitive(arg0.getTime());
			}
		});
		gson = builder.create();
	}
	

	public static Gson gson(){
		return gson;
	}
	
	public static Api api(){
		return api(true);
	}
	
	public static Api api(boolean cache){
		return cache ? cacheApi : api;
	}
	
}
