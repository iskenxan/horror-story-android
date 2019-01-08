package samatov.space.spookies.view_model.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nightonke.boommenu.BoomMenuButton;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import samatov.space.spookies.R;
import samatov.space.spookies.model.api.interfaces.ApiRequestListener;
import samatov.space.spookies.view_model.utils.BmbMenuFactory;

public abstract class BaseActivity extends AppCompatActivity {


    protected void stackFragment(Fragment fragment, int placeholder, String tag) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(placeholder, fragment, tag)
                .addToBackStack(tag).commit();
    }


    protected void replaceFragment(Fragment fragment, int placeholder) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(placeholder, fragment).commit();
    }


    protected void setupMainActionbar(Toolbar toolbar, String title) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View actionBar = mInflater.inflate(R.layout.main_action_bar, null);

        TextView titleTextView = actionBar.findViewById(R.id.toolbarTitleTextView);
        titleTextView.setText(title);

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


    public void listenToObservable(Observable observable, ApiRequestListener listener) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(requestObserver(listener));
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


    protected void handleBackPressed() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }
}
