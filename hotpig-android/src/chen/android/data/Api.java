package chen.android.data;

import chen.android.exception.FailureResponseException;
import chen.android.exception.NetworkException;
import android.graphics.Bitmap;

public interface Api {

	Account currentAccount();
	Bitmap loadAvatar(int id) throws FailureResponseException, NetworkException;
}
