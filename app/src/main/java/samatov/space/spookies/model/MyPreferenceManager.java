package samatov.space.spookies.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import samatov.space.spookies.model.api.beans.User;

public class MyPreferenceManager {


    public static String CURRENT_USER = "current_user";
    public static String SECURITY_TOKEN = "token";
    public static String USER_SEARCH_RESULT = "current_user_query_result";
    public static String CURRENT_POST_ID = "current_post_id";
    public static String CURRENT_POST_TYPE = "current_post_type";
    public static String CURRENT_POST = "current_edit_post";
    public static String CURRENT_POST_AUTHOR = "current_post_author";
    public static String CURRENT_CHAT_BUBBLE_COLOR = "current_bubble_color";
    public static String FEED_TIMELINE = "feed_timeline_list";
    public static String FEED_POPULAR = "feed_popular_list";
    public static String FAVORITE_ACTION = "like_post_action";


    private static String VIEWED_USERS = "user_searched_clicked_item";


    private static List<SharedPreferences.OnSharedPreferenceChangeListener> mListeners = new ArrayList<>();


    public static String getToken(Context context) {
      return getString(context, SECURITY_TOKEN);
    }


    public static void cleanPreferencesOnLogout(Context context) {
        delete(context, SECURITY_TOKEN);
        delete(context, CURRENT_USER);
        delete(context, VIEWED_USERS);
        delete(context, CURRENT_POST_TYPE);
        delete(context, CURRENT_POST_ID);
        delete(context, CURRENT_POST_AUTHOR);
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


    public static void addToViewedUsersStack(Context context, User viewedUser) {
        Stack<User> viewedUsers = getStackOfObjects(context, VIEWED_USERS, User.class);
        if (viewedUsers == null)
            viewedUsers = new Stack<>();

        viewedUsers.push(viewedUser);

        saveObjectAsJson(context, VIEWED_USERS, viewedUsers);
    }


    public static User peekViewedUsersStack(Context context) {
        Stack<User> viewedUsers = getStackOfObjects(context, VIEWED_USERS, User.class);

        if (viewedUsers == null || viewedUsers.isEmpty())
            return null;

        return viewedUsers.peek();
    }


    public static User popViewedUsersStack(Context context) {
        Stack<User> viewedUsers = getStackOfObjects(context, VIEWED_USERS, User.class);

        if (viewedUsers == null)
            return null;
        User user =  viewedUsers.pop();
        saveObjectAsJson(context, VIEWED_USERS, viewedUsers);

        return user;
    }


    public static void cleanViewedUsersStack(Context context) {
        MyPreferenceManager.delete(context, VIEWED_USERS);
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
        Gson gson = new GsonBuilder().serializeNulls().create();
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


    public static <T> Map<String, T> getMapOfObjects(Context context, String key, Class<T> cls) {
        String json = getString(context, key);
        Map<String, T> classMap = new Gson().fromJson(json, TypeToken.getParameterized(HashMap.class, String.class, cls).getType());

        return classMap;
    }


    public static <T> List<T> getListOfObjects(Context context, String key, Class<T> cls) {
        String jsonArray = getString(context, key);
        List<T> list = new Gson().fromJson(jsonArray, TypeToken.getParameterized(List.class, cls).getType());

        return list;
    }


    private static <T> Stack<T> getStackOfObjects(Context context, String key, Class<T> cls) {
        String jsonArray = getString(context, key);
        Stack<T> list = new Gson().fromJson(jsonArray, TypeToken.getParameterized(Stack.class, cls).getType());

        return list;
    }


    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences("GLOBAL_PREF", Context.MODE_PRIVATE);
    }
}
