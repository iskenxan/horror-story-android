package samatov.space.spookies.view_model.fragments.my_profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.utils.Formatter;
import samatov.space.spookies.view_model.activities.MyProfileActivity;


public class MyProfileFragment extends Fragment implements GalleryImagePickerListener {

    @BindView(R.id.myProfileImageView) CircleImageView mProfileImageView;
    @BindView(R.id.myProfileUsernameTextView) TextView mUsernameTextView;
    @BindView(R.id.myProfilePostsTextView) TextView mPostsTextView;
    @BindView(R.id.myProfileFollowersTextView) TextView mFollowersTextView;
    @BindView(R.id.myProfileFollowingTextView) TextView mFollowingTextView;


    MyProfileActivity mActivity;
    View mView;

    public MyProfileFragment() {}


    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, mView);
        mActivity = (MyProfileActivity) getActivity();
        setupViews();

        return mView;
    }


    private void setupViews() {
        User user  = (User) MyPreferenceManager.getObject(getActivity(), "user", User.class);
        if (!Formatter.isNullOrEmpty(user.getProfileUrl()))
            Picasso.get()
                    .load(user.getProfileUrl())
                    .resize(90,90)
                    .centerCrop().placeholder(R.drawable.ic_profile_placeholder)
                    .into(mProfileImageView);
        mUsernameTextView.setText(user.getUsername());
        mFollowersTextView.setText(user.getFollowers().size() + "\nfollowers");
        mFollowingTextView.setText(user.getFollowing().size() + "\nfollowing");
        mPostsTextView.setText(user.getPublishedRefs().size() + "\nposts");
    }


    @OnClick(R.id.myProfileImageView)
    public void onProfileImageViewClicked() {
        GalleryPickerHandler handler = new GalleryPickerHandler(mActivity, mView);
        handler.requestPermission();
    }

    @Override
    public void onImagePicked(File file) {
        Picasso.get().load(file)
                .resize(90, 90)
                .centerCrop()
                .into(mProfileImageView);
    }
}
