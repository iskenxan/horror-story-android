package samatov.space.spookies.model.api.calls;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import samatov.space.spookies.model.api.beans.Auth;

public interface AuthApi {

    @Headers( "Content-Type: application/json" )
    @POST("user/auth/login")
    Observable<Auth> login(@Body HashMap<String, Object> params);
}
