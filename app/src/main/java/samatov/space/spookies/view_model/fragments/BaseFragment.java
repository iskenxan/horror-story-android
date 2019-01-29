package samatov.space.spookies.view_model.fragments;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.IdPostRef;
import samatov.space.spookies.model.api.beans.PostRef;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.utils.FormatterK;

public class BaseFragment extends Fragment {

    protected void setProfileImage(User user, CircleImageView profileImageView) {
        String profileUrl = FormatterK.Companion.getUserProfileUrl(user.getUsername());
            Picasso.get()
                    .load(profileUrl)
                    .resize(90,90)
                    .centerCrop()
                    .error(R.drawable.ic_profile_placeholder)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .into(profileImageView);
    }


    protected void setupFollowersViews(User user, TextView followersTextView, TextView followingTextView,
                                       TextView postsTextView) {
        followersTextView.setText(user.getFollowers().size() + "\nfollowers");
        followingTextView.setText(user.getFollowing().size() + "\nfollowing");
        postsTextView.setText(user.getPublishedRefs().size() + "\nposts");
    }


    protected List<IdPostRef> getFormattedPostList(Map<String, PostRef> posts) {
        List<IdPostRef> list = new ArrayList<>();

        for (String key: posts.keySet()) {
            IdPostRef item = getIdPostRef(key, posts);
            list.add(item);
        }
        sortPostsByTimestamp(list);

        return list;
    }


    protected IdPostRef getIdPostRef(String key, Map<String, PostRef> posts) {
        IdPostRef value = new IdPostRef(posts.get(key));
        value.setId(key);

        return value;
    }


    private void sortPostsByTimestamp(List<IdPostRef> list) {
        Collections.sort(list, (a, b) ->
                (int) (b.getLastUpdated() - (a.getLastUpdated())));
    }

}
