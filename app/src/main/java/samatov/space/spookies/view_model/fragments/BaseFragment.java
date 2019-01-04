package samatov.space.spookies.view_model.fragments;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.utils.Validator;

public class BaseFragment extends Fragment {

    protected void setProfileImage(User user, CircleImageView profileImageView) {
        if (!Validator.isNullOrEmpty(user.getProfileUrl()))
            Picasso.get()
                    .load(user.getProfileUrl())
                    .resize(90,90)
                    .centerCrop().placeholder(R.drawable.ic_profile_placeholder)
                    .into(profileImageView);
    }


    protected void setupFollowersViews(User user, TextView followersTextView, TextView followingTextView,
                                       TextView postsTextView) {
        followersTextView.setText(user.getFollowers().size() + "\nfollowers");
        followingTextView.setText(user.getFollowing().size() + "\nfollowing");
        postsTextView.setText(user.getPublishedRefs().size() + "\nposts");
    }


    protected List<JsonObject> getFormattedPostList(JsonObject posts) {
        List<JsonObject> list = new ArrayList<>();

        for (String key: posts.keySet()) {
            JsonObject item = getFormattedListItem(key, posts);
            list.add(item);
        }
        sortPostsByTimestamp(list);

        return list;
    }


    private JsonObject getFormattedListItem(String key, JsonObject posts) {
        JsonObject value;
        value = posts.get(key).getAsJsonObject();
        value.addProperty("id", key);

        return value;
    }


    private void sortPostsByTimestamp(List<JsonObject> list) {
        Collections.sort(list, (a, b) ->
                (int) (b.getAsJsonPrimitive("created").getAsLong()
                        - (a.getAsJsonPrimitive("created").getAsLong())));
    }

}
