package chen.android.ui.commons;

import android.content.Intent;

public interface ActivityResultHandler {

	void handle(int requestCode, int resultCode, Intent data);
}
