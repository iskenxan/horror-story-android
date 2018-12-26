package samatov.space.spookies.model.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import samatov.space.spookies.model.api.beans.Auth;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;

public class ApiManager {

    private static String BASE_URL = "https://487be31f.ngrok.io";


    public static Retrofit getRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
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
                        .create();
    }
}
