package samatov.space.spookies.view_model.fragments.post.comment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.Comment;
import samatov.space.spookies.model.utils.TimeSince;
import samatov.space.spookies.model.utils.Validator;

public class CommentListItemViewholder extends RecyclerView.ViewHolder {

    View mContainerView;

    public CommentListItemViewholder(@NonNull View itemView) {
        super(itemView);
        mContainerView = itemView;
    }


    //TODO: some comments get wrong profile picture loaded
    public void bind(Comment comment) {
        TextView usernameTextView = mContainerView.findViewById(R.id.commentItemUsernameTextView);
        TextView textTextView = mContainerView.findViewById(R.id.commentItemTextTextView);
        TextView timestampTextView = mContainerView.findViewById(R.id.commentItemTimestampTextView);
        CircleImageView profileImageView = mContainerView.findViewById(R.id.commentItemImageView);

        usernameTextView.setText(comment.getUsername());
        textTextView.setText(comment.getText());
        String time = TimeSince.getTimeAgo(comment.getTimestamp());
        timestampTextView.setText(time);

        if (!Validator.isNullOrEmpty(comment.getProfileImageUrl()))
            Picasso.get().load(comment.getProfileImageUrl())
                    .resize(50, 50)
                    .into(profileImageView);
    }
}
