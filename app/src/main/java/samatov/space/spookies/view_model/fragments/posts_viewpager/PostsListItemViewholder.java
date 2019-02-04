package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.PostRef;
import samatov.space.spookies.model.enums.POST_TYPE;
import samatov.space.spookies.model.utils.TimeSince;

public class PostsListItemViewholder extends RecyclerView.ViewHolder {

    View mContainerView;
    PostRef mPostRef;
    boolean mDisplayTimestamp;


    public PostsListItemViewholder(@NonNull View itemView) {
        super(itemView);
        mContainerView = itemView;
    }


    public void bind(PostRef post, PostListItemClicked itemClicked, boolean displayTimeStamp, POST_TYPE postType) {
        mPostRef = post;
        mDisplayTimestamp = displayTimeStamp;

        TextView textView = mContainerView.findViewById(R.id.myProfilePostsListItemTextView);
        textView.setText(post.getTitle());

        Button button = mContainerView.findViewById(R.id.myProfilePostsListItemViewButton);
        button.setOnClickListener(view -> itemClicked.onItemClicked(post, ClickItemType.READ_POST));
        displayTimeSinceLastUpdated();

        if (postType == POST_TYPE.PUBLISHED)
            setupLikesAndComments(itemClicked);
    }


    private void setupLikesAndComments(PostListItemClicked itemClicked) {
        TextView favoriteTextView = mContainerView.findViewById(R.id.MyProfileListItemLikesTextView);
        favoriteTextView.setVisibility(View.VISIBLE);
        ImageView favoritesImageView = mContainerView.findViewById(R.id.MyProfileListItemLikesImageView);
        ImageView commentsImageView = mContainerView.findViewById(R.id.MyProfileListItemCommentImageView);

        favoriteTextView.setText(mPostRef.getFavorite().size() + "");

        favoritesImageView.setOnClickListener(view -> itemClicked.onItemClicked(mPostRef, ClickItemType.FAVORITE));
        commentsImageView.setOnClickListener(view -> itemClicked.onItemClicked(mPostRef, ClickItemType.COMMENT));
    }


    private void displayTimeSinceLastUpdated() {
        TextView timeTextView = mContainerView.findViewById(R.id.myProfilePostsListTimeTextView);
        if (mPostRef.getLastUpdated() != -1 && mDisplayTimestamp) {
            String timeSince = "Edited: " + TimeSince.getTimeAgo(mPostRef.getLastUpdated());

            timeTextView.setText(timeSince);
        } else
            timeTextView.setVisibility(View.GONE);
    }
}
