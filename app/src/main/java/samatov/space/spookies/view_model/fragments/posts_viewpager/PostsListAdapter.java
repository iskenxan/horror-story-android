package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.enums.POST_TYPE;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListItemViewholder> {

    List<JsonObject> mPosts;
    PostListItemClicked mListener;
    boolean mDisplayTimestamp;
    POST_TYPE mPostType;


    public PostsListAdapter(List<JsonObject> posts, PostListItemClicked listener,
                            boolean displayTimestamp, POST_TYPE postType) {
        this.mPosts = posts;
        this.mListener = listener;
        this.mDisplayTimestamp = displayTimestamp;
        this.mPostType = postType;
    }


    @NonNull
    @Override
    public PostsListItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_profile_post_list_item, parent, false);

        return new PostsListItemViewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PostsListItemViewholder postsListItemViewholder, int position) {
        JsonObject postJson = mPosts.get(position);
        Post post = new GsonBuilder().serializeNulls().create().fromJson(postJson, Post.class);


        postsListItemViewholder.bind(post, mListener, mDisplayTimestamp, mPostType);
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
