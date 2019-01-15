package samatov.space.spookies.view_model.dialogs.user_list;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.utils.Validator;

public class UserListItemViewHolder extends RecyclerView.ViewHolder {

    View mContainerView;

    public UserListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        mContainerView = itemView;
    }


    public void bind(String username, String profileImgUrl, UserListItemClickedListener listener) {
        CircleImageView imageView = mContainerView.findViewById(R.id.favoriteListItemImageView);
        TextView textView = mContainerView.findViewById(R.id.favoriteListItemTextView);
        ConstraintLayout clickableLayout =
                    mContainerView.findViewById(R.id.favoriteListItemClickableContainer);

        imageView.setImageResource(R.drawable.ic_profile_placeholder);
        textView.setText(username);
        if (!Validator.isNullOrEmpty(profileImgUrl))
            Picasso.get().load(profileImgUrl)
                    .resize(30, 30)
                    .into(imageView);

        clickableLayout.setOnClickListener((view) -> listener.onUserListItemClicked(username));
    }
}
