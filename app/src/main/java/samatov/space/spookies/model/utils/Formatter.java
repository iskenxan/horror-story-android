package samatov.space.spookies.model.utils;

import android.graphics.drawable.Drawable;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.HttpException;
import samatov.space.spookies.model.api.beans.ApiError;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.PostRef;

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


    public static PostRef constructRefFromPost(Post post) {
        PostRef draftRef = new PostRef(post);
        draftRef.setCreated(post.getCreated());
        draftRef.setLastUpdated(post.getLastUpdated());
        draftRef.setFavorite(new ArrayList<>(post.getFavorite().keySet()));
        draftRef.setTitle(post.getTitle());

        return draftRef;
    }


    public static Drawable drawableFromUrl(String url) throws Exception {
        Drawable dr = Drawable.createFromStream(((java.io.InputStream)
                new java.net.URL(url).getContent()), "");

        return dr;
    }
}
