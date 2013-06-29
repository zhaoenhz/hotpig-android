package chen.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import chen.android.F;
import chen.android.exception.FailureResponseException;
import chen.android.exception.NetworkException;

public class HttpResponse {

	private HttpEntity entity;
	private int statusCode;
		
	
	HttpResponse( HttpEntity entity , int statusCode) throws FailureResponseException{
		if(statusCode != 200){
			throw new FailureResponseException(statusCode);
		}	
		this.entity = entity;
		this.statusCode = statusCode;
	}
	

	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * get "Stream" as response
	 * 
	 * @return response in stream
	 * @throws NetworkException 
	 */
	public InputStream getStream() throws NetworkException  {
		InputStream ret = null;

		if (entity != null) {
			/*try {
				byte[] b = EntityUtils.toByteArray(entity);
				ret = new ByteArrayInputStream(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			} finally {
				release(entity);
			}*/
			try {
				ret = entity.getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			} catch(SocketTimeoutException ex){
				throw new NetworkException("网络连接超时",ex);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}

		return ret;
	}

	/**
	 * get "String" as response
	 * 
	 * @return response in string
	 * @throws NetworkException 
	 */
	public String getString() throws NetworkException {
		String ret = null;

		if (entity != null) {
			try {
				ret = EntityUtils.toString(entity,"UTF-8");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			} catch(SocketTimeoutException ex){
				throw new NetworkException("网络连接超时",ex);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			} finally {
				release(entity);
			}
		}

		return ret;
	}
	
	public <T> JSONResponse<T> getJson(Class<T> cls) throws NetworkException, FailureResponseException{
		return getJson(new ParameterizedTypeImpl(JSONResponse.class, new Type[]{cls}, null));
	}
	
	public <T> JSONResponse<List<T>> getJsonArray(Class<T> cls) throws NetworkException, FailureResponseException{
		return getJson(new ParameterizedTypeImpl(JSONResponse.class, new Type[]{new ParameterizedTypeImpl(List.class, new Type[]{cls}, null)}, null));
	}
	
	public <T> JSONResponse<T> getJson(Type type) throws NetworkException, FailureResponseException{
		String jsonStr = getString();
		JSONResponse<T> response = F.gson().fromJson(jsonStr, type);
		return response;
	}

	/**
	 * release connection resource
	 * 
	 * @param entity
	 */
	private static void release(HttpEntity entity) {
		try {
			entity.consumeContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
