package samatov.space.spookies.view_model.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.view_model.activities.ViewProfileActivity;
import samatov.space.spookies.view_model.fragments.posts_viewpager.PostsListAdapter;


public class ViewProfileFragment extends BaseFragment {

    public ViewProfileFragment(){}

    public static ViewProfileFragment newInstance(User user) {
        ViewProfileFragment fragment = new ViewProfileFragment();
        Bundle args = new Bundle();
        String userJson = new Gson().toJson(user, User.class);
        args.putString("user", userJson);

        fragment.setArguments(args);

        return fragment;
    }


    @BindView(R.id.viewProfileImageView) CircleImageView mProfileImageView;
    @BindView(R.id.viewProfileUsernameTextView) TextView mUsernameTextView;
    @BindView(R.id.viewProfileFollowersTextView) TextView mFollowersTextView;
    @BindView(R.id.viewProfileFollowingTextView) TextView mFollowingTextView;
    @BindView(R.id.viewProfilePostsTextView) TextView mPostsTextView;
    @BindView(R.id.viewProfileRecyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.viewProfileEmptyListContainer)LinearLayout mEmptyListContainer;


    User mUser;
    PostsListAdapter mAdapter;
    ViewProfileActivity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        ButterKnife.bind(this, view);
        getUser();
        setupViews();
        mActivity = (ViewProfileActivity) getActivity();


        return  view;
    }


    private void getUser() {
        String userJson = getArguments().getString("user");
        mUser = new Gson().fromJson(userJson, User.class);
    }


    private void setupViews() {
        setProfileImage(mUser, mProfileImageView);
        mUsernameTextView.setText(mUser.getUsername());
        setupFollowersViews(mUser, mFollowersTextView, mFollowingTextView, mPostsTextView);
        setupRecyclerView();
    }


    private void setupRecyclerView() {
        Map<String, JsonObject> posts = mUser.getPublishedRefs();
        if (posts.size() <= 0) {
            mEmptyListContainer.setVisibility(View.VISIBLE);
            return;
        }

        Gson gson = new Gson();
        String postsStr = gson.toJson(posts);

        JsonObject postsJson = gson.fromJson(postsStr, JsonObject.class);
        List<JsonObject> formattedPostsList = getFormattedPostList(postsJson);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PostsListAdapter(formattedPostsList, postId -> {
            //TODO: create a new activity for viewing other peoples posts and start it here
            MyPreferenceManager.saveString(getContext(), MyPreferenceManager.CURRENT_POST_ID, postId);
        }, false);
        mRecyclerView.setAdapter(mAdapter);
    }

}
