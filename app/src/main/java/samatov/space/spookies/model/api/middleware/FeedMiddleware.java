package samatov.space.spookies.model.api.middleware;

import android.content.Context;

import java.util.HashMap;

import io.reactivex.Observable;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.ApiManager;
import samatov.space.spookies.model.api.beans.Feed;
import samatov.space.spookies.model.api.calls.FeedApi;

public class FeedMiddleware {


    public static Observable<Feed> getMyFeed(Context context) {
        FeedApi feedApi = ApiManager.getRetrofit().create(FeedApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("token", MyPreferenceManager.getToken(context));

        return feedApi.getMyFeed(params);
    }
}
