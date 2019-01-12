package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.Post;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListItemViewholder> {

    List<JsonObject> mPosts;
    PostListItemClicked mListener;
    boolean mDisplayTimestamp;


    public PostsListAdapter(List<JsonObject> posts, PostListItemClicked listener, boolean displayTimestamp) {
        this.mPosts = posts;
        this.mListener = listener;
        this.mDisplayTimestamp = displayTimestamp;
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
//        String title = post.getAsJsonPrimitive("title").getAsString();
//        String id = post.getAsJsonPrimitive("id").getAsString();
//
//        long lastUpdated = mDisplayTimestamp ?
//                post.getAsJsonPrimitive("lastUpdated").getAsLong() : -1;

        Post post = new Gson().fromJson(postJson, Post.class);


        postsListItemViewholder.bind(post, mListener, mDisplayTimestamp);
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
