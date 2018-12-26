package samatov.space.spookies.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MyPreferenceManager {

    private static List<SharedPreferences.OnSharedPreferenceChangeListener> mListeners = new ArrayList<>();


    public static String getToken(Context context) {
      return getString(context, "token");
    }


    public static void addSharedPreferenceListener(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences preferences = getPreference(context);
        preferences.registerOnSharedPreferenceChangeListener(listener);
        mListeners.add(listener);
    }


    public static void cleanAllListeners(Context context) {
        for (SharedPreferences.OnSharedPreferenceChangeListener listener : mListeners) {
            SharedPreferences preferences = getPreference(context);
            preferences.unregisterOnSharedPreferenceChangeListener(listener);
        }
        mListeners = new ArrayList<>();
    }


    public static void delete(Context context, String key) {
        SharedPreferences preferences = getPreference(context);
        preferences.edit().remove(key).commit();
    }


    public static void saveString(Context context, String key, String value) {
        SharedPreferences preferences = getPreference(context);
        preferences.edit().putString(key, value).commit();
    }


    public static void saveObjectAsJson(Context context, String key, Object value) {
        SharedPreferences preferences = getPreference(context);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(value);
        preferences.edit().putString(key, jsonStr).commit();
    }


    public static String getString (Context context, String key) {
        SharedPreferences preferences = getPreference(context);

        return preferences.getString(key, null);
    }


    public static Object getObject(Context context, String key, Class cls) {
        String jsonStr = getString(context, key);
        if (jsonStr == null)
            return null;
        Gson gson = new Gson();

        return gson.fromJson(jsonStr, cls);
    }


    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences("GLOBAL_PREF", Context.MODE_PRIVATE);
    }
}
