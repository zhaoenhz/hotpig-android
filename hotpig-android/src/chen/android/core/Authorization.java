package chen.android.core;

import java.util.Arrays;
import java.util.List;

import chen.android.F;
import chen.android.data.Account;
import chen.android.event.Event;
import chen.android.event.EventSupport;
import chen.android.event.EventType;
import chen.android.utils.AndroidProperties;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;


public class Authorization {

	public static final String AuthFileName = "auth";
	private static AndroidProperties prop = new AndroidProperties(AuthFileName, Context.MODE_PRIVATE);
	private static String AuthCookieName1 = "curUserEmail";
	private static String AuthCookieName2 = "curUserPw";
	private static final String CookiePropertyName = "cookie";
	private static final String AccountPropertyName = "account";
	
	static{
		prop.load();
	}
	
	/**
	 * 此方法是利用可持久化的cookie来给webview授权
	 * 若没有持久化cookie，则不会授权
	 * @see Authorization
	 */
	public static void authorizeForWebview(){
        //设置webview的cookie
        String cookies = Authorization.getAuthCookie();
        if(cookies != null){
        	CookieManager cookieManager = CookieManager.getInstance(); 
        	cookieManager.setCookie(MyContext.BaseUrl+"/", cookies);  
        	CookieSyncManager.getInstance().sync(); 
        }
	}
	
	/**
	 * 取消授权，同时会删除持久化cookie
	 * 若要再次授权，需要创建持久化cookie
	 * @see Authorization
	 */
	public static void cancelAuth(){
		//清除app中的授权信息
    	clear();
		//清除cookie
		CookieManager cookieManager = CookieManager.getInstance(); 
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync(); 
		EventSupport.fire(new Event(EventType.Logout, null));
	}
	
	/**
	 * 存储cookie信息
	 * @param cookies
	 */
	public static void persistAuthCookie(List<String> cookies){
    	String cookie = extractAuthCookie(cookies);
    	prop.put(CookiePropertyName, cookie);
    	Account acc = F.api().currentAccount();
    	prop.put(AccountPropertyName, F.gson().toJson(acc));
    	prop.store();
	}
	
	private static String extractAuthCookie(List<String> cookies){
		String[] needs = new String[2];
    	for(String cookie : cookies){
        	String[] segments = cookie.split(";");
        	if(segments.length == 0) continue;
    		String pairs = segments[0].trim();
    		int idx = pairs.indexOf('=');
    		String name = pairs.substring(0, idx).trim();
    		String value = pairs.substring(idx+1).trim();
    		//只接受受权cookie
    		if(AuthCookieName1.equals(name)){
    			needs[0] = value;
    		} else if(AuthCookieName2.equals(name)){
    			needs[1] = value;
    		}
    	}
    	if(Arrays.asList(needs).contains(null)){
    		throw new RuntimeException("授权出错");
    	}
    	String cookie = AuthCookieName1 + "=" + needs[0] + ";" + AuthCookieName2 + "=" + needs[1];
    	return cookie;
	}
	
	//获取cookie
	public static String getAuthCookie(){
		return prop.getProperty(CookiePropertyName);
	}
	
	private static Account mAccount = null;
	static Account getAuthAccount(){
		if(mAccount == null){
			String json =  prop.getProperty(AccountPropertyName);
			if(json != null){
				mAccount = F.gson().fromJson(json, Account.class);
			}
		}
		return mAccount;
	}
	
	public static void updateAccount(Account acc){
		Account old = getAuthAccount();
		if(old.id == acc.id){
	    	prop.put(AccountPropertyName, F.gson().toJson(acc));
	    	prop.store();
	    	mAccount = acc;
	    	EventSupport.fire(new Event(EventType.UpdateAccountInfo, null));
		}
	}
	
	private static void clear(){
		mAccount = null;
		prop.clear();
		prop.store();
	}
	
	public static boolean hasAuth(){
		return getAuthCookie() != null && getAuthAccount() != null;
	}
}
