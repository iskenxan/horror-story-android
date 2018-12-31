package samatov.space.spookies.model.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;

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


    public static Drawable drawableFromUrl(String url) throws Exception {
        Drawable dr = Drawable.createFromStream(((java.io.InputStream)
                new java.net.URL(url).getContent()), "");

        return dr;
    }


    public static Uri uriFromUrl(String url, Context context) throws Exception {
        FutureTarget<File> futureTarget  = Glide
                .with(context.getApplicationContext())
                .load(url)
                .downloadOnly(30, 30);

        File cacheFile = futureTarget.get();
        Uri uri = Uri.fromFile(cacheFile);

        return uri;
    }
}
