package samatov.space.spookies.view_model.fragments.my_profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.utils.Validator;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;
import samatov.space.spookies.view_model.fragments.edit_post.EditPostFragment;
import samatov.space.spookies.view_model.fragments.posts_viewpager.PostsViewPagerAdapter;


public class MyProfileFragment extends Fragment implements GalleryImagePickerListener {

    @BindView(R.id.myProfileImageView) CircleImageView mProfileImageView;
    @BindView(R.id.myProfileUsernameTextView) TextView mUsernameTextView;
    @BindView(R.id.myProfilePostsTextView) TextView mPostsTextView;
    @BindView(R.id.myProfileFollowersTextView) TextView mFollowersTextView;
    @BindView(R.id.myProfileFollowingTextView) TextView mFollowingTextView;
    @BindView(R.id.myProfileViewPager) ViewPager mPostsViewPager;
    @BindView(R.id.myProfileViewPagerTabs) SmartTabLayout mViewPagerTabs;
    @BindView(R.id.myProfileFab) FloatingActionButton mFab;
    User mUser;


    MyProfileActivity mActivity;
    View mContentView;
    PostsViewPagerAdapter mViewPagerAdapter;


    public MyProfileFragment() {}


    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, mContentView);
        mActivity = (MyProfileActivity) getActivity();
        setupViews();

        return mContentView;
    }


    private void setupViews() {
        mUser = (User) MyPreferenceManager.getObject(getActivity(), "user", User.class);
        setProfileImage(mUser.getProfileUrl());
        mUsernameTextView.setText(mUser.getUsername());
        setupFollowersViews();
        setupFab();
        setupViewPager();
    }


    private void setupFollowersViews() {
        mFollowersTextView.setText(mUser.getFollowers().size() + "\nfollowers");
        mFollowingTextView.setText(mUser.getFollowing().size() + "\nfollowing");
        mPostsTextView.setText(mUser.getPublishedRefs().size() + "\nposts");
    }


    private void setProfileImage(String profileUrl) {
        if (!Validator.isNullOrEmpty(profileUrl))
            Picasso.get()
                    .load(profileUrl)
                    .resize(90,90)
                    .centerCrop().placeholder(R.drawable.ic_profile_placeholder)
                    .into(mProfileImageView);
    }


    private void setupFab() {
        mFab.setOnClickListener(event -> {
            MyPreferenceManager.delete(getContext(), EditPostActivity.CURRENT_POST_ID);
            MyPreferenceManager.delete(getContext(), EditPostFragment.CURRENT_POST);
            startEditPostActivity();
        });
    }


    public void startEditPostActivity() {
        mActivity.startEditPostActivity();
    }

    private void setupViewPager() {
        mViewPagerAdapter = new PostsViewPagerAdapter(getChildFragmentManager(), mUser);
        mPostsViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerTabs.setViewPager(mPostsViewPager);
    }


    @OnClick(R.id.myProfileImageView)
    public void onProfileImageViewClicked() {
        GalleryPickerHandler handler = new GalleryPickerHandler(mActivity, mContentView);
        handler.requestPermission();
    }

    @Override
    public void onImagePicked(File file) {
        Picasso.get().load(file)
                .resize(90, 90)
                .centerCrop()
                .into(mProfileImageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mUser = (User) MyPreferenceManager.getObject(getActivity(), "user", User.class);
        setupFollowersViews();
        mViewPagerAdapter.setUser(mUser);
        mViewPagerAdapter.notifyDataSetChanged();
    }
}
