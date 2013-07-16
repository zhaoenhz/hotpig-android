package chen.android.core;

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
        	cookieManager.setCookie(MyContext.getInstance().getBaseUrl()+"/", cookies);  
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
	public static void persistAuthCookie(Account acc, String cookie){
		if(acc == null || cookie == null || cookie.length() == 0) throw new IllegalArgumentException();
    	prop.put(CookiePropertyName, cookie);
    	prop.put(AccountPropertyName, F.gson().toJson(acc));
    	prop.store();
    	EventSupport.fire(new Event(EventType.UpdateAccountInfo, null));
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
