package samatov.space.spookies.model.api.calls;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import samatov.space.spookies.model.api.beans.Feed;
import samatov.space.spookies.model.api.beans.notification.NotificationsFeed;

public interface FeedApi {

    @POST("/feed/timeline/me")
    Observable<Feed> getMyFeed(@Body HashMap<String, Object> params);


    @POST("/feed/notification/me")
    Observable<NotificationsFeed> getNotificationFeed(@Body HashMap<String, Object> params);
}
