package samatov.space.spookies.model.api.middleware;

import android.content.Context;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Observable;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.ApiManager;
import samatov.space.spookies.model.api.beans.BasePostReference;
import samatov.space.spookies.model.api.beans.Comment;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.calls.PostsApi;

public class PostsMiddleware {


    public static Observable<Comment> addComment(BasePostReference postReference, Comment comment, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", postReference.getId());
        params.put("token", MyPreferenceManager.getToken(context));
        params.put("authorUsername", postReference.getAuthor());
        params.put("comment", comment);

        return postsApi.addComment(params);
    }


    public static Completable addToFavorite(BasePostReference postReference, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", postReference.getId());
        params.put("token", MyPreferenceManager.getToken(context));
        params.put("authorUsername", postReference.getAuthor());
        params.put("postTitle", postReference.getTitle());

        return postsApi.addToFavorite(params);
    }


    public static Completable removeFromFavorite(BasePostReference postReference, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", postReference.getId());
        params.put("token", MyPreferenceManager.getToken(context));
        params.put("authorUsername", postReference.getAuthor());

        return postsApi.removeFromFavorite(params);
    }


    public static Observable<Post> getOtherUserPost(BasePostReference postReference, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", postReference.getId());
        params.put("token", MyPreferenceManager.getToken(context));
        params.put("authorUsername", postReference.getAuthor());

        return postsApi.getOtherUserPost(params);
    }


    public static Observable<Post> getPublished(String postId, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", postId);
        params.put("token", MyPreferenceManager.getToken(context));

        return postsApi.getPublished(params);
    }


    public static Observable<Post> getDraft(String draftId, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", draftId);
        params.put("token", MyPreferenceManager.getToken(context));

        return postsApi.getDraft(params);
    }


    public static Observable<Post> updateDraft(Post draft, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("draft", draft);
        params.put("token", MyPreferenceManager.getToken(context));

        return postsApi.updateDraft(params);
    }


    public static Observable<String> deleteDraft(String draftId, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", draftId);
        params.put("token", MyPreferenceManager.getToken(context));

        return postsApi.deleteDraft(params);
    }


    public static Observable<Post> saveDraft(Post draft, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("draft", draft);
        params.put("token", MyPreferenceManager.getToken(context));

        return postsApi.saveDraft(params);
    }


    public static Observable<Post> publishPost(Post post, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("draft", post);
        params.put("token", MyPreferenceManager.getToken(context));

        return postsApi.publishPost(params);
    }


    public static Observable<Post> unpublishPost(String postId, Context context) {
        PostsApi postsApi = ApiManager.getRetrofit().create(PostsApi.class);

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", postId);
        params.put("token", MyPreferenceManager.getToken(context));

        return postsApi.unpublishPost(params);
    }
}
