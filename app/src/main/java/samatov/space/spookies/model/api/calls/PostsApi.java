package samatov.space.spookies.model.api.calls;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import samatov.space.spookies.model.api.beans.Post;

public interface PostsApi {

    @POST("user/posts/draft/save")
    Observable<Post> saveDraft(@Body HashMap<String, Object> params);

    @POST("user/posts/draft/update")
    Observable<Post> updateDraft(@Body HashMap<String, Object> params);

    @POST("user/posts/draft/delete")
    Observable<String> deleteDraft(@Body HashMap<String, Object> params);

    @POST("user/posts/draft/publish")
    Observable<Post> publishPost(@Body HashMap<String, Object> params);

    @POST("user/posts/published/unpublish")
    Observable<Post> unpublishPost(@Body HashMap<String, Object> params);

    @POST("user/posts/draft/get")
    Observable<Post> getDraft(@Body HashMap<String, Object> params);

    @POST("user/posts/published/get")
    Observable<Post> getPublished(@Body HashMap<String, Object> params);

    @POST("posts/get")
    Observable<Post> getOtherUserPost(@Body HashMap<String, Object> params);

    @POST("posts/add-favorite")
    Completable addToFavorite(@Body HashMap<String, Object> params);

    @POST("posts/remove-favorite")
    Completable removeFromFavorite(@Body HashMap<String, Object> params);
}
