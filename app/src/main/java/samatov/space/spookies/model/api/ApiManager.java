package samatov.space.spookies.model.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import samatov.space.spookies.model.api.beans.Auth;
import samatov.space.spookies.model.api.beans.User;

public class ApiManager {

    private static String BASE_URL = "https://b5cf9b4a.ngrok.io/";


    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getConverter()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }


    private static Gson getConverter() {
        return new GsonBuilder()
                        .registerTypeAdapter(Auth.class, new MyDeserializer<Auth>())
                        .registerTypeAdapter(User.class, new MyDeserializer<User>())
                        .registerTypeAdapter(String.class, new MyDeserializer<String>())
                        .create();
    }
}
