package samatov.space.spookies.view_model.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.io.File;
import java.util.List;

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
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.view_model.fragments.my_profile.MyProfileFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class MyProfileActivity extends BaseActivity {

    @BindView(R.id.myProfileToolbar) Toolbar mToolbar;
    AppCompatActivity mActivity;

    MyProfileFragment myProfileFragment;
    SweetAlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        setupActionBar(mToolbar);
        myProfileFragment = MyProfileFragment.newInstance();
        replaceFragment(myProfileFragment, R.id.myProfilePlaceholder);

        mActivity = this;
    }


    public void startEditPostActivity() {
        ActivityFactory.startActivity(this, EditPostActivity.class, true, false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
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
                User user = (User) MyPreferenceManager.getObject(mActivity, "user", User.class);
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
