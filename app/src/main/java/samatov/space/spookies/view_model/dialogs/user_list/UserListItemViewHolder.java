package samatov.space.spookies.view_model.dialogs.user_list;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.utils.FormatterK;
import samatov.space.spookies.model.utils.Validator;

public class UserListItemViewHolder extends RecyclerView.ViewHolder {

    View mContainerView;

    public UserListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        mContainerView = itemView;
    }


    public void bind(String username, UserListItemClickedListener listener) {
        CircleImageView imageView = mContainerView.findViewById(R.id.favoriteListItemImageView);
        TextView textView = mContainerView.findViewById(R.id.favoriteListItemTextView);
        ConstraintLayout clickableLayout =
                    mContainerView.findViewById(R.id.favoriteListItemClickableContainer);

        String profileImgUrl = FormatterK.Companion.getUserProfileUrl(username);
        imageView.setImageResource(R.drawable.ic_profile_placeholder);
        textView.setText(username);
        if (!Validator.isNullOrEmpty(profileImgUrl))
            Picasso.get().load(profileImgUrl)
                    .resize(30, 30)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(imageView);

        clickableLayout.setOnClickListener((view) -> listener.onUserListItemClicked(username));
    }
}
