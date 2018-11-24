package samatov.space.spookies.model.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Validator {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.equals("null") || str.equals("");
    }


    public static boolean isJsonString(String str) {
        try {
            new JSONObject(str);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}
