package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.fragments.ViewProfileFragment;
import samatov.space.spookies.view_model.fragments.post.comment.CommentFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class ViewProfileActivity extends BaseActivity {


    @BindView(R.id.viewProfileToolbar) Toolbar mToolbar;
    User mUser;
    SweetAlertDialog mDialog;
    ViewProfileActivity mActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ButterKnife.bind(this);
        mActivity = this;

        setupMainActionbar(mToolbar, "View profile");
        getCurrentUser();
        replaceFragment(ViewProfileFragment.newInstance(), R.id.viewProfileMainPlaceholder);
    }


    private void getCurrentUser() {
        mUser = MyPreferenceManager
                .getObject(this, MyPreferenceManager.CURRENTLY_VIEWING_USER, User.class);
    }


    public void startReadPostActivity(String postId) {
        MyPreferenceManager.saveString(this, MyPreferenceManager.CURRENT_POST_AUTHOR, mUser.getUsername());
        MyPreferenceManager.saveString(this, MyPreferenceManager.CURRENT_POST_ID, postId);
        ActivityFactory.startActivity(this, ReadPostActivity.class, true, false);
    }


    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }


    public void startReadCommentFragment(Post post, String authorUsername) {
        mToolbar.setVisibility(View.GONE);
        MyPreferenceManager.saveObjectAsJson(this, MyPreferenceManager.CURRENT_POST, post);
        MyPreferenceManager.saveString(this, MyPreferenceManager.CURRENT_POST_AUTHOR, authorUsername);
        stackFragment(CommentFragment.newInstance(this, true), R.id.viewProfileMainPlaceholder, "current_post");
    }


    public void followSelectedUser(ApiRequestListener listener) {
        displayLoadingDialog();
        Observable<User> observable = User.follow(this, mUser.getUsername(), mUser.getProfileUrl());
        listenToObservable(observable, (result, exception) ->
                onRequestComplete(result, exception, listener));
    }


    public void unfollowSelectedUser(ApiRequestListener listener) {
        displayLoadingDialog();
        Observable<User> observable = User.unfollow(this, mUser.getUsername());
        listenToObservable(observable, (result, exception) ->
                onRequestComplete(result, exception, listener));
    }


    private void onRequestComplete(Object result, Throwable exception, ApiRequestListener listener) {
        mDialog.dismiss();
        if (exception != null)
            displayErrorDialog();
        else {
            User currentUser = (User) result;
            MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_USER, currentUser);
        }
        listener.onRequestComplete(result, exception);
    }


    private void displayLoadingDialog() {
        mDialog = DialogFactory.getLoadingDialog(this, "Performing your request...");
        mDialog.show();
    }


    private void displayErrorDialog() {
        String errorText = "Couldn't complete your request. Please try again later.";
        mDialog = DialogFactory.getErrorDialog(this, errorText, null);
        mDialog.show();
    }


    @Override
    public void onBackPressed() {
        if (!handleBackPressed()) {
            MyPreferenceManager.delete(this, MyPreferenceManager.CURRENTLY_VIEWING_USER);
            finishAfterTransition();
        }
    }
}
