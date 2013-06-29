package chen.android.utils;

public class Action {
	
	public static enum Method {POST, GET}
	
	Action(Method method, String url){
		this.url = url;
		this.method = method;
	}
	
	public static Action post(String url){
		return new Action(Method.POST, url);
	}
	
	public static Action get(String url){
		return new Action(Method.GET, url);
	}
	
	private String url;
	private Method method;
	public String getUrl() {
		return url;
	}

	void setUrl(String url) {
		this.url = url;
	}

	public Method getMethod() {
		return method;
	}

	void setMethod(Method method) {
		this.method = method;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (method != other.method)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
	
}
