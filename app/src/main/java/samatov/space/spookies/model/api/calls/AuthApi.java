package samatov.space.spookies.model.api.calls;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import samatov.space.spookies.model.api.beans.Auth;

public interface AuthApi {

    @POST("user/auth/login")
    Observable<Auth> login(@Body HashMap<String, Object> params);


    @POST("user/auth/signup")
    Observable<Auth> signup(@Body HashMap<String, Object> params);
}
