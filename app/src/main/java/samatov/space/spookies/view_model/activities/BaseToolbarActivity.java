package samatov.space.spookies.view_model.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

public  abstract class BaseToolbarActivity extends BaseActivity {
    protected Toolbar mToolbar;


    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }


    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
    }
}
