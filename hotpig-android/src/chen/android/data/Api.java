package chen.android.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.Header;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import chen.android.core.MyContext;
import chen.android.data.cache.FileCacheFactory;
import chen.android.exception.FailureResponseException;
import chen.android.exception.NetworkException;
import chen.android.utils.Action;
import chen.android.utils.HttpRequest;
import chen.android.utils.HttpRequest.ResponseHeaderHandler;
import chen.android.utils.JSONResponse;

public class Api {

	private static final String BaseUrl = MyContext.getInstance().getBaseUrl() + "/api/";
	
	private boolean cache;
	public Api(boolean cache){
		this.cache = cache;
	}
	
	public String signIn(String email, String password) throws FailureResponseException, NetworkException{
		CookieHandler handler = new CookieHandler();
		post("sign-in").setResponseHeaderHandler(handler)
						.addParam("email", email)
						.addParam("password", password)
						.execute().getJson(Object.class).checkSuccess();
		String cookie = handler.mCookie;
		if(cookie == null) throw new FailureResponseException(JSONResponse.errorMessage("授权失败"));
		return cookie;
	}

	public Account currentAccount(String cookie) throws FailureResponseException, NetworkException{
		return get("current-account").addHeader("Cookie", cookie).execute().getJson(Account.class).getReturnObject();
	}

	public Bitmap loadAvatar(int id) throws FailureResponseException,
			NetworkException {
		// TODO Auto-generated method stub
		if(cache){
			Bitmap bm = FileCacheFactory.getAvatar(id);
			if(bm!=null) return bm;
		}
		InputStream is = new HttpRequest(Action.get(MyContext.getInstance().getBaseUrl() + "/static/photo/"+id+"-small.jpg")).execute().getStream();
		Bitmap bm = BitmapFactory.decodeStream(is);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		if(cache){
			FileCacheFactory.putAvatar(id, bm);
		}
		return bm;
	}
	
	public int checkIn(int taskId, String content, Star star, boolean failed) throws FailureResponseException, NetworkException{
		return post("check-in").addParam("id", taskId).addParam("content", content).addParam("star", star==null?0:star.ordinal()+1).addParam("failed", failed).execute().getJson(Integer.class).getReturnObject();
	}
	
	public List<TaskThread> listTask(int accId) throws FailureResponseException, NetworkException{
		return get("list-task").addParam("id", accId).execute().getJsonArray(TaskThread.class).getReturnObject();
	}
	
	public List<Checkin> listCheckin(int taskId) throws FailureResponseException, NetworkException{
		return get("list-checkin").addParam("id", taskId).execute().getJsonArray(Checkin.class).getReturnObject();
	}

	private HttpRequest get(String uri){
		return new HttpRequest(Action.get(BaseUrl + uri));
	}
	
	private HttpRequest post(String uri){
		return new HttpRequest(Action.post(BaseUrl + uri));
	}
	
	private class CookieHandler implements ResponseHeaderHandler {
		String mCookie = null;
		@Override
		public void handle(Header[] headers) {
			// TODO Auto-generated method stub
			if(headers == null) return;
			for(Header header : headers){
				if("Set-Cookie".equals(header.getName())){
					String cookie = header.getValue();
					if(cookie == null) continue;
					String[] segments = cookie.split(";");
					String[] pairs = segments[0].split("=");
					if(pairs.length != 2) continue;
					if("session".equals(pairs[0])){
						mCookie = pairs[0] + "=" + pairs[1];
						break;
					}
				}
			}
		}
	}
}
