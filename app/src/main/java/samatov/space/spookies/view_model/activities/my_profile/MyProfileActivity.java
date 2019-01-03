package samatov.space.spookies.view_model.activities.my_profile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import samatov.space.spookies.model.api.middleware.SearchMiddleware;
import samatov.space.spookies.model.utils.Validator;
import samatov.space.spookies.view_model.activities.BaseActivity;
import samatov.space.spookies.view_model.activities.EditPostActivity;
import samatov.space.spookies.view_model.fragments.my_profile.MyProfileFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;

public class MyProfileActivity extends BaseActivity {

    @BindView(R.id.myProfileToolbar) Toolbar mToolbar;
    AppCompatActivity mActivity;

    MyProfileFragment myProfileFragment;
    SweetAlertDialog mDialog;
    List<User> mUsers;

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
        setupSearchAction(menu);

        return true;
    }


    private void setupSearchAction(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search for users...");

        SearchUsersCursorAdapter cursorAdapter = new SearchUsersCursorAdapter(this, null, 0);
        searchView.setSuggestionsAdapter(cursorAdapter);
        setupQueryListenerForSearchView(searchView, cursorAdapter);
    }


    private void setupQueryListenerForSearchView(SearchView searchView, SearchUsersCursorAdapter adapter) {
        RxSearchView.queryTextChanges(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    String query = charSequence + "";
                    requestSearch(query, adapter);
                });
    }


    private void requestSearch(String query, SearchUsersCursorAdapter adapter) {
        try {
            if (Validator.isNullOrEmpty(query))
                return;

            listenToObservable(SearchMiddleware.searchForUsers(query, mActivity), (result, exception) -> {
                mUsers = (List<User>) result;
                MyPreferenceManager.saveObjectAsJson(mActivity,
                        MyPreferenceManager.CURRENT_USER_QUERY_RESULTS, mUsers);
                Cursor cursor = createCursorFromResult();
                adapter.swapCursor(cursor);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Cursor createCursorFromResult()  {
        String[] menuCols = new String[] {BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_ICON_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA };

        MatrixCursor cursor = new MatrixCursor(menuCols);
        int counter = 0;

        for (User user : mUsers) {
            cursor.addRow(new Object[]{ counter, user.getUsername(), user.getProfileUrl(), user.getUsername() });
            counter++;
        }

        return cursor;
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
