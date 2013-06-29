package chen.android.utils;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import chen.android.core.Authorization;
import chen.android.core.MyContext;
import chen.android.exception.FailureResponseException;
import chen.android.exception.NetworkException;

import android.util.Log;

public class HttpRequest {
	
	private final static int TIMEOUT_CONNECTION = 8000;
	private final static int TIMEOUT_SOCKET = 16000;
	private final static int RETRY_COUNT = 3;
	
	
	private Action action;
	private HashMap<String, String> mHeaders = null;
	private HttpEntity mBody = null;
	private int mStatus = -1;
	private ResponseHeaderHandler handler;
	
	public HttpRequest(Action action){
		this.action = new Action(action.getMethod(), action.getUrl());
	}

	public HttpRequest addHeader(String key, String value) {
		if (mHeaders == null)
			mHeaders = new HashMap<String, String>();
		mHeaders.put(key, value);
		return this;
	}

	public void clearHeader() {
		mHeaders.clear();
		mHeaders = null;
	}

	private Map<String, Object> params = null;
	public HttpRequest addParam(String name, Object value){
		if(params == null){
			params = new HashMap<String, Object>();
		}
		params.put(name, value);
		return this;
	}
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
	private static String _MakeURL(String p_url, Map<String, ?> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}

		return url.toString().replace("?&", "?");
	}

	public void setPostBody(String body) {
		try {
			mBody = new StringEntity(body);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public HttpRequest setHttpEntity(HttpEntity entity){
		mBody = entity;
		return this;
	}
	
	public HttpRequest setResponseHeaderHandler(ResponseHeaderHandler handler){
		this.handler = handler;
		return this;
	}

	public chen.android.utils.HttpResponse execute() throws NetworkException, FailureResponseException  {
		HttpEntity entity = httprequest();
		return new chen.android.utils.HttpResponse(entity, this.mStatus);
	}

	/**
	 * get "HttpEntity" as response
	 * 
	 * @return
	 * @throws NetworkException 
	 */
	private HttpEntity httprequest() throws NetworkException{
		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT_SOCKET);

		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, true));

		// check get or post method by params
		HttpRequestBase method = null;
		if (action.getMethod() == Action.Method.GET) {
			if(params != null && params.size() > 0)  action.setUrl(_MakeURL(action.getUrl(), params));
			method = new HttpGet(action.getUrl());
			Log.i(this.getClass().getName(), "GET " + action.getUrl());
		} else {
			if(params != null && params.size() > 0){
				List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
				for(Map.Entry<String, ?> entry : params.entrySet()){
					list.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
				}
				try {
					mBody = new UrlEncodedFormEntity(list, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}		
			}
			method = new HttpPost(action.getUrl());
			Log.i(this.getClass().getName(), "POST " + action.getUrl());
			((HttpPost) method).setEntity(mBody);
		}

		// set request header
		if (mHeaders != null) {
			Iterator<?> iter = mHeaders.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<?, ?> entry = (Entry<?, ?>) iter.next();
				String key = (String) entry.getKey();
				String val = (String) entry.getValue();
				method.setHeader(key, val);
			}
		}
		
		//keep alive
		method.setHeader(HTTP.CONN_DIRECTIVE,HTTP.CONN_KEEP_ALIVE);
		//os
		method.setHeader("os", "android");
		//sdk int
		method.setHeader("sdk-int", String.valueOf(android.os.Build.VERSION.SDK_INT));
		//device name
		method.setHeader("device-name", android.os.Build.MODEL);
		//app version
		method.setHeader("app-version", String.valueOf(MyContext.getInstance().getAppVersion()));
		//api version
		method.setHeader("api-version", String.valueOf(1.0));
		// set auth cookie
		if(Authorization.hasAuth()){
			method.addHeader("Cookie", Authorization.getAuthCookie());	
		}
		
		// get response
		HttpResponse response = null;;
		try {
			response = client.execute(method);
		} catch(ConnectTimeoutException e){
			throw new NetworkException("网络连接超时", e);
		} catch(SocketTimeoutException e){
			throw new NetworkException("网络连接超时", e);
		} catch (ClientProtocolException e) {
			throw new NetworkException(e.getMessage(), e);
		} catch (UnknownHostException e) {
			throw new NetworkException("未知主机地址"+e.getMessage(), e);
		} catch (IOException e) {
			throw new NetworkException(e.getMessage(), e);
		}
		mStatus = response.getStatusLine().getStatusCode();
		Log.i(this.getClass().getName(), "status code: " + mStatus);
		if(handler != null){
			handler.handle(response.getAllHeaders());
		}
		HttpEntity entity = response.getEntity();
		return entity;
	}

	public interface ResponseHeaderHandler{
		void handle(Header[] headers);
	}

	}

