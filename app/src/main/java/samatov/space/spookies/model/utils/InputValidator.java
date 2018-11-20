package samatov.space.spookies.model.utils;

import android.widget.EditText;

public class InputValidator {

    public static boolean inputNotEmpty(EditText editText) {
        String text = editText.getText() + "";

        return text != null && text.length() > 2;
    }


}
