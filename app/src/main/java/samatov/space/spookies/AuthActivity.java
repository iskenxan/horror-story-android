package samatov.space.spookies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import samatov.space.spookies.view.fragments.LoginFragment;


public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        startFragment(LoginFragment.newInstance(this));
    }


    public void startFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.authMainPlaceholder, fragment).commit();
    }
}
