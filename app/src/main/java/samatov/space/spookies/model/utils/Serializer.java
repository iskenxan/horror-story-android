package samatov.space.spookies.model.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Serializer {


    public static <T> T toObject(String jsonStr, Class<T> cls) {
        if (jsonStr == null)
            return null;
        Gson gson = new Gson();

        return gson.fromJson(jsonStr, cls);
    }


    public static <T> Map<String, T> toMapOfObjects(String json, Class<T> cls) {
        Map<String, T> classMap = new Gson()
                .fromJson(json, TypeToken.getParameterized(HashMap.class, String.class, cls).getType());

        return classMap;
    }


    public static <T> List<T> toListOfObjects(String jsonArray, Class<T> cls) {
        List<T> list = new Gson()
                .fromJson(jsonArray, TypeToken.getParameterized(List.class, cls).getType());

        return list;
    }


    public static <T> Stack<T> toStackOfObjects(String jsonArray, Class<T> cls) {
        Stack<T> list = new Gson()
                .fromJson(jsonArray, TypeToken.getParameterized(Stack.class, cls).getType());

        return list;
    }



    public static String toString(Object object) {
        return new GsonBuilder().serializeNulls().create().toJson(object);
    }
}
