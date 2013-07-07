package chen.android.utils;

import java.io.File;

public class Utils {

	public static int deleteDir(File file){
		return _deleteDir(file, new IntWrapper()).n;
	}
	
	private static IntWrapper _deleteDir(File file, IntWrapper wrapper){
		if(file.isDirectory()){
			File[] children = file.listFiles();
			for(File child : children){
				_deleteDir(child, wrapper);
			}
		} else {
			file.delete();
			++ wrapper.n;
		}
		return wrapper;
	}
	
	private static class IntWrapper{
		int n;
	}
	
	public static String ellipsize(String input, int maxLength) {
		  if (input == null || input.length() < maxLength) {
		    return input;
		  }
		  return input.substring(0, maxLength) + "...";
	}
	
}
