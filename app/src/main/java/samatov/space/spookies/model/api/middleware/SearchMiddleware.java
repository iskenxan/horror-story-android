package samatov.space.spookies.model.api.middleware;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.ApiManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.calls.SearchApi;

public class SearchMiddleware {


    public static Observable<Map<String, User>> searchForUsers(String query, Context context) {
        SearchApi searchApi = ApiManager.getRetrofit().create(SearchApi.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", MyPreferenceManager.getToken(context));
        params.put("query", query);

        return searchApi.searchForUsers(params);
    }


    public static Observable<Map<String, User>> searchSuggested(Context context) {
        SearchApi searchApi = ApiManager.getRetrofit().create(SearchApi.class);
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", MyPreferenceManager.getToken(context));

        return searchApi.searchSuggested(params);
    }
}
