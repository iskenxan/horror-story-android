package samatov.space.spookies.model.api.middleware;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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


    public static Observable<String> saveProfilePicture(File imageFile, String token) {
        ProfileApi profileApi = ApiManager.getRetrofit().create(ProfileApi.class);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);

        return profileApi.saveProfileImage(filePart, tokenBody);
    }

}
