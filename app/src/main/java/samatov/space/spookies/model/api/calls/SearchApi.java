package samatov.space.spookies.model.api.calls;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import samatov.space.spookies.model.api.beans.User;

public interface SearchApi {


    @POST("/search/users")
    Observable<Map<String, User>> searchForUsers(@Body HashMap<String, Object> params);
}
