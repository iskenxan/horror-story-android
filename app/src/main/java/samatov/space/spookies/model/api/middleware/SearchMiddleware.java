package samatov.space.spookies.model.api.middleware;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.ApiManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.calls.SearchApi;

public class SearchMiddleware {


    public static Observable<List<User>> searchForUsers(String query, Context context) {
        SearchApi searchApi = ApiManager.getRetrofit().create(SearchApi.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", MyPreferenceManager.getToken(context));

        return searchApi.searchForUsers(params);
    }


    public static List<User> searchForUsersSync(String query, Context context) throws IOException {
        SearchApi searchApi = ApiManager.getRetrofit().create(SearchApi.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", MyPreferenceManager.getToken(context));
        params.put("query", query);

        Response<List<User>> response = searchApi.searchForUsersSync(params).execute();

        return response.body();
    }
}
