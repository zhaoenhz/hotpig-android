package chen.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import chen.android.core.MyContext;

import android.content.Context;

public class AndroidProperties extends Properties{

	private static final long serialVersionUID = 1L;
	
	private String fileName;	
	private int mode;
	public AndroidProperties(String fileName, int mode){
		this.fileName = fileName;
		this.mode = mode;
	}
	
	public void load() {
		Context context = MyContext.getInstance().getApp();
		if(exists()){ 
			try {
				FileInputStream fis = context.openFileInput(fileName);
				this.load(fis);
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
	}
	
	public void store(){
		Context context = MyContext.getInstance().getApp();
		try {
			FileOutputStream fis = context.openFileOutput(fileName, mode);
			this.store(fis, null);
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private File getFile(){
		return MyContext.getInstance().getApp().getFileStreamPath(fileName);
	}
	
	public boolean exists(){
		return getFile().exists();
	}
	
}
