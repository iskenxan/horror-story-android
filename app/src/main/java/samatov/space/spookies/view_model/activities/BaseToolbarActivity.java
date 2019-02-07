package samatov.space.spookies.view_model.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;

import samatov.space.spookies.R;

public  abstract class BaseToolbarActivity extends BaseActivity {
    protected Toolbar mToolbar;
    protected Stack<String> mTitles = new Stack<>();


    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }


    public void hideToolbar() {
        if (mToolbar != null)
            mToolbar.setVisibility(View.GONE);
    }


    protected void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowHomeEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
    }


    public void setMainToolbarTitle(String newTitle) {
        mToolbar.setVisibility(View.VISIBLE);
        TextView textView = mToolbar.findViewById(R.id.toolbarTitleTextView);
        if (textView == null)
            textView = mToolbar.findViewById(R.id.readPostToolbarTitleTextView);
        if (textView == null)
            return;

        textView.setText(newTitle);
        mTitles.push(newTitle);
    }


    public void checkFragmentAndResetTitle() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("comment_fragment");
        if (fragment != null && fragment.isVisible())
            return;

        if (mTitles.size() <= 1)
            return;

        mTitles.pop();
        setMainToolbarTitle(mTitles.peek());
    }
}
