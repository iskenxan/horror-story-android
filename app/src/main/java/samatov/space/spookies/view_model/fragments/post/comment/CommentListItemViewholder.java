package samatov.space.spookies.view_model.fragments.post.comment;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.Comment;
import samatov.space.spookies.model.utils.FormatterK;
import samatov.space.spookies.model.utils.TimeSince;

public class CommentListItemViewholder extends RecyclerView.ViewHolder {

    View mContainerView;

    public CommentListItemViewholder(@NonNull View itemView) {
        super(itemView);
        mContainerView = itemView;
    }


    public void bind(Comment comment, CommentClickedListener listener) {
        TextView usernameTextView = mContainerView.findViewById(R.id.commentItemUsernameTextView);
        TextView textTextView = mContainerView.findViewById(R.id.commentItemTextTextView);
        TextView timestampTextView = mContainerView.findViewById(R.id.commentItemTimestampTextView);
        CircleImageView profileImageView = mContainerView.findViewById(R.id.commentItemImageView);
        ConstraintLayout clickableContainer = mContainerView.findViewById(R.id.commentItemClickableContainer);


        usernameTextView.setText(comment.getUsername());
        textTextView.setText(comment.getText());
        String time = TimeSince.getTimeAgo(comment.getTimestamp());
        timestampTextView.setText(time);

        profileImageView.setImageResource(R.drawable.ic_profile_placeholder);
        String profileUrl = FormatterK.Companion.getUserProfileUrl(comment.getUsername());
            Picasso.get()
                    .load(profileUrl)
                    .error(R.drawable.ic_profile_placeholder)
                    .resize(50, 50)
                    .into(profileImageView);

        clickableContainer.setOnClickListener((view) -> listener.onCommentClicked(comment.getUsername()));
    }
}
