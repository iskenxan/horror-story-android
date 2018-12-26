package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import java.util.List;

import samatov.space.spookies.R;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListItemViewholder> {

    List<JsonObject> mPosts;
    PostListItemClicked mListener;


    public PostsListAdapter(List<JsonObject> posts, PostListItemClicked listener) {
        this.mPosts = posts;
        mListener = listener;
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
        JsonObject post = mPosts.get(position);
        String title = post.getAsJsonPrimitive("title").getAsString();
        String id = post.getAsJsonPrimitive("id").getAsString();
        long lastUpdated = post.getAsJsonPrimitive("lastUpdated").getAsLong();

        postsListItemViewholder.bind(title, mListener, id, lastUpdated);
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
