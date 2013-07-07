package chen.android.data.cache;

import java.io.File;
import java.io.InputStream;

public interface Converter<T> {

	InputStream convert(T t);
	
	T convert(File file);
}
