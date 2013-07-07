package chen.android.data.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapConverter implements Converter<Bitmap>{

	@Override
	public InputStream convert(Bitmap t) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		t.compress(Bitmap.CompressFormat.PNG, 85, baos);
		return new ByteArrayInputStream(baos.toByteArray());
	}

	@Override
	public Bitmap convert(File file) {
		// TODO Auto-generated method stub
		return BitmapFactory.decodeFile(file.getAbsolutePath());
	}

}
