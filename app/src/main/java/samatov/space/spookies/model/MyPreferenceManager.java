package samatov.space.spookies.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import samatov.space.spookies.model.utils.ListOfJson;

public class MyPreferenceManager {


    public static String CURRENT_USER = "current_user";
    public static String SECURITY_TOKEN = "token";
    public static String USER_SEARCH_RESULT = "current_user_query_result";
    public static String USER_SEARCH_CLICKED_ITEM = "user_searched_clicked_item";
    public static String CURRENT_POST_ID = "current_post_id";
    public static String CURRENT_POST_TYPE = "current_post_type";
    public static String CURRENT_POST = "current_edit_post";


    private static List<SharedPreferences.OnSharedPreferenceChangeListener> mListeners = new ArrayList<>();


    public static String getToken(Context context) {
      return getString(context, "token");
    }


    public static void cleanPreferencesOnLogout(Context context) {
        delete(context, SECURITY_TOKEN);
        delete(context, CURRENT_USER);
        delete(context, USER_SEARCH_CLICKED_ITEM);
        delete(context, CURRENT_POST_TYPE);
        delete(context, CURRENT_POST_ID);
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


    public static <T> T getObject(Context context, String key, Class<T> cls) {
        String jsonStr = getString(context, key);
        if (jsonStr == null)
            return null;
        Gson gson = new Gson();

        return gson.fromJson(jsonStr, cls);
    }


    public static <T> List<T> getListOfObjects(Context context, String key, Class<T> cls) {
        String jsonArray = getString(context, key);
        List<T> yourClassList = new Gson().fromJson(jsonArray, new ListOfJson<>(cls));

        return yourClassList;
    }


    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences("GLOBAL_PREF", Context.MODE_PRIVATE);
    }
}
