package samatov.space.spookies.model.utils;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.HttpException;
import samatov.space.spookies.model.api.beans.ApiError;
import samatov.space.spookies.model.api.beans.Post;

public class Formatter {

    public static ApiError extractErrorData(HttpException e) throws Exception {
        ApiError error = new ApiError(400, "There was a connection problem. Please check your connection and try again.");
        if (e.response().errorBody() == null)
            return error;
        String responseStr = e.response().errorBody().string();
        if (!Validator.isJsonString(responseStr))
            return error;
        JSONObject responseError = new JSONObject(responseStr).getJSONObject("error");
        switch (responseError.getInt("status")) {
            case 401:
            case 404:
            case 400:
                error.setMessage(responseError.getString("message"));
                break;
            default:
                error.setMessage("There was an issue on the server. Please try again later.");
        }

        return error;
    }


    public static JsonObject constructRefFromPost(Post post) {
        JsonObject draftRef = new JsonObject();
        draftRef.addProperty("created", post.getCreated());
        draftRef.addProperty("lastUpdated", post.getLastUpdated());
        draftRef.addProperty("title", post.getTitle());

        return draftRef;
    }
}
