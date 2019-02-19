package samatov.space.spookies.model.utils;

import android.widget.EditText;

import java.util.regex.Pattern;

public class InputValidator {

    public static boolean inputNotEmpty(EditText editText) {
        String text = editText.getText() + "";

        return text != null && text.length() > 2;
    }


    public static boolean hasSpecialCharacters(String text) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        boolean hasSpecialChar = p.matcher(text).find();

        return hasSpecialChar;
    }
}
