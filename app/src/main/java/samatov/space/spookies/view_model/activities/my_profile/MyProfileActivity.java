package samatov.space.spookies.view_model.activities.my_profile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Post;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.activities.BaseToolbarActivity;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.activities.ViewProfileActivity;
import samatov.space.spookies.view_model.fragments.my_profile.MyProfileFragment;
import samatov.space.spookies.view_model.fragments.post.comment.CommentFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class MyProfileActivity extends BaseToolbarActivity {

    @BindView(R.id.myProfileToolbar) Toolbar mToolbar;
    AppCompatActivity mActivity;

    MyProfileFragment myProfileFragment;
    SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        mActivity = this;
        super.mToolbar = this.mToolbar;
        setupMainActionbar(mToolbar, "My profile");
        myProfileFragment = MyProfileFragment.newInstance();
        replaceFragment(myProfileFragment, R.id.myProfilePlaceholder);
    }


    public void startEditPostActivity() {
        ActivityFactory.startActivity(this, EditPostActivity.class, true, false);
    }


    public void startViewCommentFragment(Post post) {
        mToolbar.setVisibility(View.GONE);
        User currentUser = MyPreferenceManager
                .getObject(this, MyPreferenceManager.CURRENT_USER, User.class);
        MyPreferenceManager.saveObjectAsJson(this, MyPreferenceManager.CURRENT_POST, post);
        MyPreferenceManager.saveString(this, MyPreferenceManager.CURRENT_POST_AUTHOR, currentUser.getUsername());
        CommentFragment fragment = CommentFragment.newInstance(this, true);
        stackFragment(fragment, R.id.myProfilePlaceholder, "comment_fragment");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setupSearchAction(menu);

        return true;
    }


    private void setupSearchAction(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        SearchUserHandler searchHandler = new SearchUserHandler(searchView,
                searchManager, this, onSearchResultListener(), onSearchItemClickListener());
    }


    private ApiRequestListener onSearchResultListener() {
        return (result, exception) -> {
            if (exception != null) {
                String text = "Error performing your search, please try again later";
                mDialog = DialogFactory.getErrorDialog(this, text, null);
                mDialog.show();
                return;
            }
        };
    }


    private OnSearchSuggestionClick onSearchItemClickListener() {
        return (username) -> {
            Map<String, User> users = MyPreferenceManager.getMapOfObjects(mActivity,
                    MyPreferenceManager.USER_SEARCH_RESULT, User.class);
            User user = users.get(username);
            startViewUserProfileActivity(user);
        };
    }


    private void startViewUserProfileActivity(User user) {
        MyPreferenceManager.addToViewedUsersStack(this, user);
        ActivityFactory.startActivity(mActivity,
                ViewProfileActivity.class, true, false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                mDialog = DialogFactory.getLoadingDialog(mActivity, "Saving profile picture...");
                mDialog.show();
                saveProfileImage(imageFiles.get(0));
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
            }
        });
    }


    private void saveProfileImage(File file) {
        String token = MyPreferenceManager.getString(this, "token");
        User.saveProfilePicture(file, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onProfileImageSaved(file));
    }


    private Observer<String> onProfileImageSaved(File file) {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String profileUrl) {
                User user = MyPreferenceManager.getObject(mActivity, "user", User.class);
                user.setProfileUrl(profileUrl);
                MyPreferenceManager.saveObjectAsJson(mActivity, "user", user);
                myProfileFragment.onImagePicked(file);
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mDialog.dismiss();
                String errorText = "Error saving your profile picture. Please try again";
                mDialog = DialogFactory.getErrorDialog(mActivity, errorText, null);
                mDialog.show();
            }

            @Override
            public void onComplete() {

            }
        };
    }


    @Override
    public void onBackPressed() {
        handleBackPressed();
    }
}
