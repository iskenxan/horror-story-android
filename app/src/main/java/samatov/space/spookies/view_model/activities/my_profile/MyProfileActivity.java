package samatov.space.spookies.view_model.activities.my_profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.IdPostRef;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.view_model.activities.BaseToolbarActivity;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.fragments.my_profile.MyProfileFragment;
import samatov.space.spookies.view_model.utils.ActionbarItemsManager;
import samatov.space.spookies.view_model.utils.ActivityFactory;

public class MyProfileActivity extends BaseToolbarActivity {

    @BindView(R.id.myProfileToolbar) Toolbar mToolbar;
    MyProfileActivity mActivity;

    MyProfileFragment myProfileFragment;

    ActionbarItemsManager mManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        mActivity = this;
        super.mToolbar = this.mToolbar;
        setupMainActionbar(mToolbar, "My profile");
        mPlaceholder = R.id.myProfilePlaceholder;
        myProfileFragment = MyProfileFragment.newInstance();
        replaceFragment(myProfileFragment, R.id.myProfilePlaceholder);
    }


    public void startEditPostActivity() {
        ActivityFactory.startActivity(this, EditPostActivity.class, true, false);
    }


    public void startViewCommentFragment(IdPostRef post) {
        mToolbar.setVisibility(View.GONE);
        User currentUser = MyPreferenceManager
                .getObject(this, MyPreferenceManager.CURRENT_USER, User.class);
        fetchPostAndStartReadCommentFragment(post.getId(),
                currentUser.getUsername(), R.id.myProfilePlaceholder);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_action_bar_menu, menu);

        mManager = new ActionbarItemsManager(this, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mManager.onActionBarItemClicked(item);
        return super.onOptionsItemSelected(item);
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
                displayLoadingDialog();
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
                MyPreferenceManager.saveObjectAsJson(mActivity, "user", user);
                myProfileFragment.onImagePicked(file);
                mDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {
                mDialog.dismiss();
                displayErrorDialog();
            }

            @Override
            public void onComplete() {

            }
        };
    }


    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("notification_fragment");
        if (fragment != null && fragment.isVisible())
            setMainToolbarTitle("My Profile");
        handleBackPressed();
    }
}
