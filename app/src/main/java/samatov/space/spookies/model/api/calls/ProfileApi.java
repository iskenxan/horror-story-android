package samatov.space.spookies.model.api.calls;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import samatov.space.spookies.model.api.beans.User;

public interface ProfileApi {

    @POST("user/profile/me")
    Observable<User> getUserInfo(@Body HashMap<String, Object> params);


    @POST("user/profile/other")
    Observable<User> getOtherUserInfo(@Body HashMap<String, Object> params);


    @POST("user/profile/follow")
    Observable<User> follow(@Body HashMap<String, Object> params);


    @POST("user/profile/unfollow")
    Observable<User> unfollow(@Body HashMap<String, Object> params);


    @Multipart
    @POST("user/profile/profile-image/save")
    Observable<String> saveProfileImage(@Part MultipartBody.Part file, @Part("token")RequestBody token);
}
