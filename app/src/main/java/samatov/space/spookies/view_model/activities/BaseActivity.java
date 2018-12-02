package samatov.space.spookies.view_model.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.nightonke.boommenu.BoomMenuButton;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.utils.BmbMenuFactory;

public abstract class BaseActivity extends AppCompatActivity {

    protected void startFragment(Fragment fragment, int placeholder) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(placeholder, fragment).commit();
    }


    protected void setupActionBar(Toolbar toolbar) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View actionBar = mInflater.inflate(R.layout.custom_action_bar, null);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setCustomView(actionBar);
        bar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);
        BoomMenuButton bmb = actionBar.findViewById(R.id.toolbarBmb);
        BmbMenuFactory.setupBmb(this, bmb);
    }


    protected Observer<Object> requestObserver(ApiRequestListener listener) {
        return new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object auth) {
               listener.onRequestComplete(auth, null);
            }

            @Override
            public void onError(Throwable e) {
                listener.onRequestComplete(null, e);
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
