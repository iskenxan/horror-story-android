package samatov.space.spookies.view_model.fragments.posts_viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import samatov.space.spookies.R;
import samatov.space.spookies.model.utils.TimeSince;

public class PostsListItemViewholder extends RecyclerView.ViewHolder {

    View mContainerView;

    public PostsListItemViewholder(@NonNull View itemView) {
        super(itemView);
        mContainerView = itemView;
    }


    public void bind(String title, PostListItemClicked itemClicked, String postId, long lastUpdated) {
        TextView textView = mContainerView.findViewById(R.id.myProfilePostsListItemTextView);
        textView.setText(title);

        String timeSince = "Edited: " + TimeSince.getTimeAgo(lastUpdated);
        TextView timeTextView = mContainerView.findViewById(R.id.myProfilePostsListTimeTextView);
        timeTextView.setText(timeSince);

        Button button = mContainerView.findViewById(R.id.myProfilePostsListItemViewButton);
        button.setOnClickListener(view -> itemClicked.onItemClicked(postId));
    }
}
