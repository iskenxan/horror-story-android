package samatov.space.spookies.view_model.fragments.view_profile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.PostRef;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.enums.POST_TYPE;
import samatov.space.spookies.model.utils.FormatterK;
import samatov.space.spookies.view_model.activities.ViewProfileActivity;
import samatov.space.spookies.view_model.dialogs.favorite.FavoriteDialogHandler;
import samatov.space.spookies.view_model.dialogs.user_list.UserListDialogHandler;
import samatov.space.spookies.view_model.fragments.BaseFragment;
import samatov.space.spookies.view_model.fragments.posts_viewpager.ClickItemType;
import samatov.space.spookies.view_model.fragments.posts_viewpager.PostListItemClicked;
import samatov.space.spookies.view_model.fragments.posts_viewpager.PostsListAdapter;


public class ViewProfileFragment extends BaseFragment {

    public ViewProfileFragment() {}

    public static ViewProfileFragment newInstance() {
        ViewProfileFragment fragment = new ViewProfileFragment();

        return fragment;
    }


    @BindView(R.id.viewProfileImageView) CircleImageView mProfileImageView;
    @BindView(R.id.viewProfileUsernameTextView) TextView mUsernameTextView;
    @BindView(R.id.viewProfileFollowersTextView) TextView mFollowersTextView;
    @BindView(R.id.viewProfileFollowingTextView) TextView mFollowingTextView;
    @BindView(R.id.viewProfilePostsTextView) TextView mPostsTextView;
    @BindView(R.id.viewProfileRecyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.viewProfileEmptyListContainer)LinearLayout mEmptyListContainer;
    @BindView(R.id.viewProfileFollowButton) Button mFollowButton;


    PostsListAdapter mAdapter;
    ViewProfileActivity mActivity;
    User mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        ButterKnife.bind(this, view);
        mActivity = (ViewProfileActivity) getActivity();


        return  view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getUser();
        mActivity.showToolbar();
        setupViews();
    }


    private void getUser() {
        User user = MyPreferenceManager.peekViewedUsersStack(getContext());
        if (user != null)
            mUser = user;
    }


    private void setupViews() {
        setProfileImage(mUser, mProfileImageView, false);
        mUsernameTextView.setText(mUser.getUsername());
        setupFollowersViews(mUser, mFollowersTextView, mFollowingTextView, mPostsTextView);
        setupRecyclerView();
        if (followingSelectedUser())
            setButtonToUnfollow();
    }


    private void setupRecyclerView() {
        Map<String, PostRef> posts = mUser.getPublishedRefs();
        if (posts.size() <= 0) {
            mEmptyListContainer.setVisibility(View.VISIBLE);
            return;
        }

        List<PostRef> formattedPostsList = FormatterK.Companion.getFormattedPostList(posts);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new PostsListAdapter(formattedPostsList,
                false, POST_TYPE.PUBLISHED, getOnItemClicked());
        mRecyclerView.setAdapter(mAdapter);
    }


    private PostListItemClicked getOnItemClicked() {
        return (postRef, clickType) -> {
            if (clickType == ClickItemType.READ_POST)
                mActivity.startReadPostActivity(postRef);
            else if (clickType == ClickItemType.COMMENT) {
                mActivity.fetchPostAndStartReadCommentFragment(postRef);
            } else if (clickType == ClickItemType.FAVORITE) {
                FavoriteDialogHandler handler = new FavoriteDialogHandler(mActivity, postRef.getFavorite(), postRef.getTitle());
                handler.showDialog();
            }
        };
    }


    @OnClick(R.id.viewProfileFollowersTextView)
    public void onFollowersTextViewClicked() {
        UserListDialogHandler handler = new UserListDialogHandler(mActivity, mUser.getFollowers(), "Followers");
        handler.showDialog();
    }


    @OnClick(R.id.viewProfileFollowingTextView)
    public void onFollowingTextViewClicked() {
        UserListDialogHandler handler = new UserListDialogHandler(mActivity, mUser.getFollowing(), "Following");
        handler.showDialog();
    }


    @OnClick(R.id.viewProfileFollowButton)
    public void onFollowClicked() {
        if (!followingSelectedUser())
            followSelectedUser();
        else
            unFollowSelectedUser();
    }


    private void followSelectedUser() {
        mActivity.followSelectedUser((result, exception) -> {
            if (exception != null)
                return;
            setButtonToUnfollow();
            addCurrentUserToFollowers();
            setupFollowersViews(mUser, mFollowersTextView, mFollowingTextView, mPostsTextView);
        });
    }


    private void unFollowSelectedUser() {
        mActivity.unfollowSelectedUser((result, exception) -> {
            if (exception != null)
                return;
            setButtonToFollow();
            removeCurrentUserFromFollowers();
            setupFollowersViews(mUser, mFollowersTextView, mFollowingTextView, mPostsTextView);
        });
    }


    private void setButtonToUnfollow() {
        mFollowButton.setText("Unfollow");
        mFollowButton.setBackgroundResource(R.drawable.neutral_btn_bg);
        mFollowButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }


    private void setButtonToFollow() {
        mFollowButton.setText("Follow");
        mFollowButton.setBackgroundResource(R.drawable.action_btn_bg);
        mFollowButton.setTextColor(Color.WHITE);
    }


    private boolean followingSelectedUser() {
        User currentUser = MyPreferenceManager
                .getObject(getContext(), MyPreferenceManager.CURRENT_USER, User.class);
       return mUser.getFollowers().contains(currentUser.getUsername());
    }


    private void removeCurrentUserFromFollowers() {
        User currentUser = MyPreferenceManager
                .getObject(getContext(), MyPreferenceManager.CURRENT_USER, User.class);
        mUser.getFollowers().remove(currentUser.getUsername());
        updateSearchResultItem(); // we're updating the search result list because the searchView caches the most current results for a quick load
        MyPreferenceManager.popViewedUsersStack(mActivity);
        MyPreferenceManager.addToViewedUsersStack(mActivity, mUser);
    }


    private void addCurrentUserToFollowers() {
        User currentUser = MyPreferenceManager
                .getObject(mActivity, MyPreferenceManager.CURRENT_USER, User.class);
        mUser.getFollowers().add(currentUser.getUsername());
        updateSearchResultItem();
        MyPreferenceManager.popViewedUsersStack(mActivity);
        MyPreferenceManager.addToViewedUsersStack(mActivity, mUser);
    }


    private void updateSearchResultItem() {
        String resultKey = MyPreferenceManager.USER_SEARCH_RESULT;
        Map<String, User> users = MyPreferenceManager.getMapOfObjects(mActivity, resultKey, User.class);
        if (users != null) {
            users.put(mUser.getUsername(), mUser);
            MyPreferenceManager.saveObjectAsJson(getContext(), resultKey, users);
        }
    }
}
