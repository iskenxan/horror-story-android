package samatov.space.spookies.model.api.middleware;

import android.content.Context;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.ApiManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.calls.ProfileApi;

public class ProfileMiddleware {


    public static Observable<User> getUserInfo(String token) {
        ProfileApi profileApi = ApiManager.getRetrofit().create(ProfileApi.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);

        return profileApi.getUserInfo(params);
    }


    public static Observable<User> follow(Context context, String followingUsername, String followingProfileUrl) {
        ProfileApi profileApi = ApiManager.getRetrofit().create(ProfileApi.class);
        User currentUser = MyPreferenceManager
                .getObject(context, MyPreferenceManager.CURRENT_USER, User.class);
        String token = MyPreferenceManager.getToken(context);

        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("user", currentUser);
        params.put("followingUsername", followingUsername);
        params.put("followingProfileUrl", followingProfileUrl);


        return profileApi.follow(params);
    }


    public static Observable<User> unfollow(Context context, String followingUsername) {
        ProfileApi profileApi = ApiManager.getRetrofit().create(ProfileApi.class);
        User currentUser = MyPreferenceManager
                .getObject(context, MyPreferenceManager.CURRENT_USER, User.class);
        String token = MyPreferenceManager.getToken(context);

        HashMap<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("user", currentUser);
        params.put("followingUsername", followingUsername);


        return profileApi.unfollow(params);
    }


    public static Observable<String> saveProfilePicture(File imageFile, String token) {
        ProfileApi profileApi = ApiManager.getRetrofit().create(ProfileApi.class);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(),
                RequestBody.create(MediaType.parse("image/*"), imageFile));
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        return profileApi.saveProfileImage(filePart, tokenBody);
    }

}
