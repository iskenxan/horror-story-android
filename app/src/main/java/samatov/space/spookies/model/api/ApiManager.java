package samatov.space.spookies.model.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import samatov.space.spookies.model.api.beans.Auth;
import samatov.space.spookies.model.api.beans.Comment;
import samatov.space.spookies.model.api.beans.Feed;
import samatov.space.spookies.model.api.beans.FeedItem;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.beans.notification.NotificationActivity;
import samatov.space.spookies.model.api.beans.notification.NotificationGroup;
import samatov.space.spookies.model.api.beans.notification.NotificationsFeed;

public class ApiManager {

    private static String BASE_URL = "https://spookies-server.herokuapp.com";
//    private static String BASE_URL = "https://57768e98.ngrok.io";


    public static Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getConverter()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    private static Gson getConverter() {
        return new GsonBuilder()
                        .registerTypeAdapter(Auth.class, new MyDeserializer<Auth>())
                        .registerTypeAdapter(User.class, new MyDeserializer<User>())
                        .registerTypeAdapter(String.class, new MyDeserializer<String>())
                        .registerTypeAdapter(Post.class, new MyDeserializer<Post>())
                        .registerTypeAdapter(List.class, new MyDeserializer<List>())
                        .registerTypeAdapter(Map.class, new MyDeserializer<Map>())
                        .registerTypeAdapter(Comment.class, new MyDeserializer<Comment>())
                        .registerTypeAdapter(Feed.class, new MyDeserializer<Feed>())
                        .registerTypeAdapter(FeedItem.class, new MyDeserializer<FeedItem>())
                        .registerTypeAdapter(NotificationsFeed.class, new MyDeserializer<NotificationsFeed>())
                        .registerTypeAdapter(NotificationGroup.class, new MyDeserializer<NotificationGroup>())
                        .registerTypeAdapter(NotificationActivity.class, new MyDeserializer<NotificationActivity>())
                        .create();
    }
}
