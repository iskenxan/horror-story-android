package samatov.space.spookies.model.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.File;

import retrofit2.HttpException;
import samatov.space.spookies.model.api.beans.ApiError;

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


    public static boolean isNullOrEmpty(String str) {
        return str == null || str.equals("") || str.equals("null");
    }


    public static Bitmap extractBitmapFromFile(File file) {
        if (file.exists())
           return BitmapFactory.decodeFile(file.getAbsolutePath());

        return null;
    }
}
