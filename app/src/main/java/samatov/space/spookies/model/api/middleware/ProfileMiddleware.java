package samatov.space.spookies.model.api.middleware;

import java.util.HashMap;

import io.reactivex.Observable;
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

}
