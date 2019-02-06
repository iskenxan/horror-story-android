package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.BasePostReference;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity;
import samatov.space.spookies.view_model.fragments.view_profile.ViewProfileFragment;
import samatov.space.spookies.view_model.utils.ActionbarItemsManager;
import samatov.space.spookies.view_model.utils.ActivityFactory;

public class ViewProfileActivity extends BaseToolbarActivity {


    @BindView(R.id.viewProfileToolbar) Toolbar mToolbar;
    User mUser;
    ViewProfileActivity mActivity;
    ActionbarItemsManager mManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ButterKnife.bind(this);
        mActivity = this;

        super.mToolbar = this.mToolbar;

        mPlaceholder = R.id.viewProfileMainPlaceholder;
        setupMainActionbar(mToolbar, "View profile");
        getCurrentUser();
        replaceFragment(ViewProfileFragment.newInstance(), R.id.viewProfileMainPlaceholder);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar_menu, menu);
        mManager = new ActionbarItemsManager(this, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mManager.onActionBarItemClicked(item);

        return super.onOptionsItemSelected(item);
    }


    private void getCurrentUser() {
        User currentUser = MyPreferenceManager
                .getObject(this, MyPreferenceManager.CURRENT_USER, User.class);

        mUser = MyPreferenceManager.peekViewedUsersStack(this);
        if (mUser.getUsername().equals(currentUser.getUsername())) {
            MyPreferenceManager.cleanViewedUsersStack(this);
            ActivityFactory.startActivity(this, MyProfileActivity.class, true, true);
        }
    }

    public void startReadPostActivity(BasePostReference postReference) {
        MyPreferenceManager.saveObjectAsJson(mActivity, MyPreferenceManager.CURRENT_POST_REF, postReference);
        ActivityFactory.startActivity(this, ReadPostActivity.class, true, false);
    }


    public void fetchPostAndStartReadCommentFragment(BasePostReference postReference) {
        mToolbar.setVisibility(View.GONE);
        fetchPostAndStartReadCommentFragment(postReference, R.id.viewProfileMainPlaceholder);
    }


    public void followSelectedUser(ApiRequestListener listener) {
        displayLoadingDialog();
        Observable<User> observable = User.follow(this, mUser.getUsername());
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


    @Override
    public void onBackPressed() {
        checkIfCurrentFragmentNotificationAndResetToolbarTitle("View profile");
        if (!handleBackPressed()) {
            MyPreferenceManager.popViewedUsersStack(this);
            finishAfterTransition();
        }
    }
}
