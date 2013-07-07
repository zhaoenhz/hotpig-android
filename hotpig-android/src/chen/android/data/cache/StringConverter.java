package chen.android.data.cache;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class StringConverter implements Converter<String>{

	@Override
	public InputStream convert(String t) {
		// TODO Auto-generated method stub
		return new ByteArrayInputStream(t.getBytes());
	}

	@Override
	public String convert(File file) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		try {
			FileReader reader = new FileReader(file);
			char[] buf = new char[256];
			for(int i=reader.read(buf); i!=-1; i=reader.read(buf)){
				sb.append(buf,0,i);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

}
