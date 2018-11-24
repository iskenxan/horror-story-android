package samatov.space.spookies.model.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class MyDeserializer<T> implements JsonDeserializer<T>
{
    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException
    {
        // Get the "result" element from the parsed JSON
        JsonElement content = je.getAsJsonObject().get("result");

        // Deserialize it. You use a new instance of Gson to avoid infinite recursion
        // to this deserializer
        return new Gson().fromJson(content, type);

    }
}