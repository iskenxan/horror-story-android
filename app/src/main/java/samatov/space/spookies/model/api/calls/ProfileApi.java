package samatov.space.spookies.model.api.calls;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import samatov.space.spookies.model.api.beans.User;

public interface ProfileApi {

    @POST("user/profile/me")
    Observable<User> getUserInfo(@Body HashMap<String, Object> params);
}
