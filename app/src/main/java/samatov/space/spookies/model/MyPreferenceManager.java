package samatov.space.spookies.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class MyPreferenceManager {

    public static void saveString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("GLOBAL_PREF", Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
    }

    public static void saveObjectAsJson(Context context, String key, Object value) {
        SharedPreferences preferences = context.getSharedPreferences("GLOBAL_PREF", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(value);
        preferences.edit().putString(key, jsonStr).commit();
    }


    public static String getString (Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("GLOBAL_PREF", Context.MODE_PRIVATE);

        return preferences.getString(key, null);
    }


    public static Object getObject(Context context, String key, Class cls) {
        String jsonStr = getString(context, key);
        if (jsonStr == null)
            return null;
        Gson gson = new Gson();

        return gson.fromJson(jsonStr, cls);
    }
}
