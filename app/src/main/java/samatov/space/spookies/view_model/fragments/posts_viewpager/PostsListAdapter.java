package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.PostRef;
import samatov.space.spookies.model.enums.POST_TYPE;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListItemViewholder> {

    List<PostRef> mPosts;
    PostListItemClicked mListener;
    boolean mDisplayTimestamp;
    POST_TYPE mPostType;


    public PostsListAdapter(List<PostRef> posts,
                            boolean displayTimestamp, POST_TYPE postType, PostListItemClicked listener) {
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
        PostRef post = mPosts.get(position);

        postsListItemViewholder.bind(post, mListener, mDisplayTimestamp, mPostType);
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }
}
