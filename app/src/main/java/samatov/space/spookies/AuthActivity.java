package samatov.space.spookies;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import samatov.space.spookies.model.MyPreferenceManager;
import samatov.space.spookies.model.api.beans.Auth;
import samatov.space.spookies.view.fragments.LoginFragment;


public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        checkIfLoggedInAndRedirect();
    }


    private void checkIfLoggedInAndRedirect() {
        String token = MyPreferenceManager.getString(getApplicationContext(), "token");
        if (token == null)
            startFragment(LoginFragment.newInstance(this));
        else {
            Intent myIntent = new Intent(AuthActivity.this, MyProfileActivity.class);
            startActivity(myIntent);
        }
    }


    public void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.authMainPlaceholder, fragment).commit();
    }


    public void onAuthSuccess(Auth auth) {
        MyPreferenceManager.saveString(getApplicationContext(), "token", auth.getToken());
        MyPreferenceManager.saveObjectAsJson(getApplicationContext(), "user", auth.getUser());
        animateToProfileActivity();
    }


    private void animateToProfileActivity() {
        Intent myIntent = new Intent(AuthActivity.this, MyProfileActivity.class);
        startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
