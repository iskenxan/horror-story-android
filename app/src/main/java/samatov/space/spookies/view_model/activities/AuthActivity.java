package samatov.space.spookies.view_model.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Auth;
import samatov.space.spookies.model.api.beans.User;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
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
        mPlaceholder = R.id.authMainPlaceholder;
        checkIfLoggedInAndRedirect();
    }


    private void checkIfLoggedInAndRedirect() {
        String token = MyPreferenceManager
                .getString(getApplicationContext(), MyPreferenceManager.SECURITY_TOKEN);
        if (token == null)
            startFragment(LoginFragment.newInstance(this));
        else
            fetchUserData(token);
    }


    public void startFragment(Fragment fragment) {
        replaceFragment(fragment, R.id.authMainPlaceholder);
    }


    public void fetchUserData(String token) {
        mDialog.show();
        listenToObservable(User.getUserInfo(token), (result, exception) -> {
            mDialog.dismiss();
            if (exception != null)
                onFetchUserError();
            else
                onFetchUserSuccess(result);
        });
    }


    private void onFetchUserError() {
        String errorMessage = "Error retrieving your data. Please try logging in again.";
        SweetAlertDialog errorDialog = DialogFactory.getErrorDialog(this, errorMessage, dialog -> {
            dialog.dismiss();
            MyPreferenceManager.delete(this, MyPreferenceManager.SECURITY_TOKEN);
            MyPreferenceManager.delete(this, MyPreferenceManager.CURRENT_USER);
            startFragment(LoginFragment.newInstance(this));
        });
        errorDialog.show();
    }


    private void onFetchUserSuccess(Object result) {
        User user = (User) result;
        MyPreferenceManager.saveObjectAsJson(this, MyPreferenceManager.CURRENT_USER, user);
        ActivityFactory.startActivity(this, FeedActivity.class, true, true);
    }


    public void startLogin(String username, String password, AuthListener listener) {
        mDialog.show();
        Observable observable = Auth.login(username, password);
        listenToObservable(observable, onAuthComplete(listener));
    }


    public void startSignup(String username, String password, String repeatPassword, AuthListener listener) {
        mDialog.show();
        Observable observable = Auth.signup(username, password, repeatPassword);
        listenToObservable(observable, onAuthComplete(listener));
    }


    private ApiRequestListener onAuthComplete(AuthListener listener) {
        return (result, e) -> {
            mDialog.dismiss();
            if (e != null)
                listener.onAuthError(e);
            else
                onAuthSuccess((Auth) result);
        };
    }


    private void onAuthSuccess(Auth auth) {
        MyPreferenceManager.saveString(getApplicationContext(), MyPreferenceManager.SECURITY_TOKEN, auth.getToken());
        MyPreferenceManager.saveObjectAsJson(getApplicationContext(), MyPreferenceManager.CURRENT_USER, auth.getUser());
        ActivityFactory.startActivity(this, FeedActivity.class, true, true);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
