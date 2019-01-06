package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.fragments.ViewProfileFragment;
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

        setupActionBar(mToolbar, "View profile");
        getCurrentUser();
        replaceFragment(ViewProfileFragment.newInstance(mUser), R.id.viewProfileMainPlaceholder);
    }


    private void getCurrentUser() {
        mUser = MyPreferenceManager
                .getObject(this, MyPreferenceManager.USER_SEARCH_CLICKED_ITEM, User.class);
    }


    public void followSelectedUser(ApiRequestListener listener) {
        displayLoadingDialog();
        Observable<User> observable = User.follow(this, mUser.getUsername(), mUser.getProfileUrl());
        listenToObservable(observable, (result, exception) -> {
            onRequestComplete(result, exception, listener);
        });
    }


    public void unfollowSelectedUser(ApiRequestListener listener) {
        displayLoadingDialog();
        Observable<User> observable = User.unfollow(this, mUser.getUsername());
        listenToObservable(observable, (result, exception) -> {
            onRequestComplete(result, exception, listener);
        });
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
}
