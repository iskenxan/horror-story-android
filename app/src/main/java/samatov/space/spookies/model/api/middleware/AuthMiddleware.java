package samatov.space.spookies.model.api.middleware;

import java.util.HashMap;

import io.reactivex.Observable;
import samatov.space.spookies.model.api.ApiManager;
import samatov.space.spookies.model.api.beans.Auth;
import samatov.space.spookies.model.api.calls.AuthApi;

public class AuthMiddleware {


    public static Observable<Auth> login(String username, String password) {
        AuthApi twitterApi = ApiManager.getRetrofit().create(AuthApi.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        return twitterApi.login(params);
    }


    public static Observable<Auth> signup(String username, String password, String repeatPassword) {
        AuthApi twitterApi = ApiManager.getRetrofit().create(AuthApi.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("repeatPassword", repeatPassword);

        return twitterApi.signup(params);
    }

}
