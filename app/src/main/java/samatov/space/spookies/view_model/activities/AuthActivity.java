package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Auth;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.interfaces.AuthListener;
import samatov.space.spookies.view_model.fragments.LoginFragment;
import samatov.space.spookies.view_model.utils.ActivityFactory;
import samatov.space.spookies.view_model.utils.DialogFactory;


public class AuthActivity extends BaseActivity {

    SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mDialog = DialogFactory.getLoadingDialog(this, "Loading...");
        checkIfLoggedInAndRedirect();
    }


    private void checkIfLoggedInAndRedirect() {
        String token = MyPreferenceManager.getString(getApplicationContext(), "token");
        if (token == null)
            startFragment(LoginFragment.newInstance(this));
        else
            fetchUserData(token);
    }


    public void startFragment(Fragment fragment) {
        startFragment(fragment, R.id.authMainPlaceholder);
    }


    public void fetchUserData(String token) {
        mDialog.show();
        User.getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestObserver((result, e) -> {
                    mDialog.dismiss();
                    if (e != null)
                        onFetchUserError();
                    else
                        onFetchUserSuccess(result);
        }));
    }


    private void onFetchUserError() {
        String errorMessage = "There was an error retrieving your data. Please try logging in again.";
        mDialog = DialogFactory.getErrorDialog(this, errorMessage, dialog -> {
            dialog.dismiss();
            MyPreferenceManager.deleteString(this, "token");
            startFragment(LoginFragment.newInstance(this));
        });
    }


    private void onFetchUserSuccess(Object result) {
        User user = (User) result;
        MyPreferenceManager.saveObjectAsJson(this, "user", user);
        ActivityFactory.startActivity(this, MyProfileActivity.class, true);
    }


    public void startLogin(String username, String password, AuthListener listener) {
        mDialog.show();
        Auth.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onAuthComplete(listener));
    }


    public void startSignup(String username, String password, String repeatPassword, AuthListener listener) {
        mDialog.show();
        Auth.signup(username, password, repeatPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onAuthComplete(listener));
    }


    private Observer<Object> onAuthComplete(AuthListener listener) {
        return requestObserver((result, e) -> {
            mDialog.dismiss();
            if (e != null)
                listener.onAuthError(e);
            else
                onAuthSuccess((Auth) result);
        });
    }


    private void onAuthSuccess(Auth auth) {
        MyPreferenceManager.saveString(getApplicationContext(), "token", auth.getToken());
        MyPreferenceManager.saveObjectAsJson(getApplicationContext(), "user", auth.getUser());
        ActivityFactory.startActivity(this, MyProfileActivity.class, true);
    }

}